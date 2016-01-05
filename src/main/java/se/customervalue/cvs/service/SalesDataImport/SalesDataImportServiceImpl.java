package se.customervalue.cvs.service.SalesDataImport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.CountryRepository;
import se.customervalue.cvs.abstraction.dataaccess.CurrencyRepository;
import se.customervalue.cvs.abstraction.dataaccess.SalesDataRepository;
import se.customervalue.cvs.abstraction.dataaccess.TransactionRepository;
import se.customervalue.cvs.common.CVSConfig;
import se.customervalue.cvs.domain.*;
import se.customervalue.cvs.domain.Currency;

import javax.transaction.Transactional;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SalesDataImportServiceImpl implements SalesDataImportService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SalesDataRepository salesDataRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Override @Async @Transactional
	public void start(File file, int salesDataId) {
		SalesData salesData = salesDataRepository.findBySalesDataId(salesDataId);
		if(isValid(file, salesData)) {
			importTransactions(preprocessTransactions(file, salesData), salesData);
			// TODO: Maybe clean up temporary files!
		} else {
			log.error("[Sales Data Import Service] Error validating sales data file " + file.getAbsolutePath());
			salesData.setStatus(SalesDataStatus.ERROR);
			salesDataRepository.save(salesData);
		}
	}

	private boolean isValid(File file, SalesData salesData) {
		log.debug("[Sales Data Import Service] Validating file " + file.getAbsolutePath());

		boolean salesDataFileIsValid = true;
		try {
			long startTime = System.nanoTime();

			// File Validation Process
			String edgeID = "\\d{1,20}";
			String country = "\"[A-Z]{1,3}\"";
			String ordDate = "\\d{4}-\\d{2}-\\d{2}";
			String ordYear = "\\d{4}";
			String currency = "\"[A-Z]{2,3}\"";
			String amount = "-?\\d{0,6}(\\.\\d{1,10})?";

			String[] sdPeriodSplit = salesData.getSalesPeriod().split(" ");
			String month = String.valueOf(Arrays.asList(CVSConfig.months).indexOf(sdPeriodSplit[0]));
			String year = sdPeriodSplit[1];
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date earliestAllowedDate = dateFormat.parse(year + "-" + month + "-01 00:00:00");
			Calendar c = Calendar.getInstance();
			c.setTime(earliestAllowedDate);
			Date latestAllowedDate = dateFormat.parse(year + "-" + month + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59");

			try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				String line = bufferedReader.readLine();
				if(line == null || !line.equals("edgeid\tcountry\tord_date\tord_year\tcurrency\tamount")) {
					log.error("[Sales Data Import Service] Invalid header line " + line);
					salesDataFileIsValid = false;
				}

				int lineNumber = 1;
				while((line = bufferedReader.readLine()) != null) {
					lineNumber++;
					if(line.matches(edgeID + "\\t" + country + "\\t" + ordDate + "\\t" + ordYear + "\\t" + currency + "\\t" + amount)) {
						String[] data = line.split("\t");
						Date transactionDate = dateFormat.parse(data[2] + " 00:00:00");
						if(transactionDate.before(earliestAllowedDate) || transactionDate.after(latestAllowedDate)) {
							log.error("[Sales Data Import Service] Invalid date on line " + lineNumber);
							salesDataFileIsValid = false;
							break;
						}
					} else {
						log.error("[Sales Data Import Service] Invalid transaction format on line " + lineNumber);
						salesDataFileIsValid = false;
						break;
					}
				}
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000;
			log.debug("[PROFILING::" + salesData.getSalesDataId() + "] Validation: " + duration + "ms");
		} catch (IOException | ParseException ex) {
			salesDataFileIsValid = false;
			salesData.setStatus(SalesDataStatus.ERROR);
			salesDataRepository.save(salesData);
		}

		return salesDataFileIsValid;
	}

	private File preprocessTransactions(File file, SalesData salesData) {
		File processedTransactions = new File(CVSConfig.TEMP_FS_STORE + "\\" + salesData.getSalesDataId() + ".txt");

		Map<String, String> countryToID = new HashMap<>();
		Map<String, String> currencyToID = new HashMap<>();

		try {
			long startTime = System.nanoTime();
			try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(processedTransactions));
				bufferedWriter.write("edgeid\tcountry\tord_date\tord_year\tcurrency\tamount\n");
				String inLine = bufferedReader.readLine();
				while((inLine = bufferedReader.readLine()) != null) {
					String[] data = inLine.split("\t");

					// Replace Country ISO code with Country ID
					String countryCodeIso31661a2 = data[1].substring(1, 3);
					if(countryToID.containsKey(countryCodeIso31661a2)) {
						data[1] = countryToID.get(countryCodeIso31661a2);
					} else {
						log.debug("[Sales Data Import Service] Looking up " + countryCodeIso31661a2 + " country ISO code!");
						Country country = countryRepository.findByIso31661a2(countryCodeIso31661a2);
						countryToID.put(countryCodeIso31661a2, String.valueOf(country.getCountryId()));
						data[1] = String.valueOf(country.getCountryId());
					}

					// Replace Currency ISO code with Currency ID
					String currencyCodeIso4217 = data[4].substring(1, 4);
					if(currencyToID.containsKey(currencyCodeIso4217)){
						data[4] = currencyToID.get(currencyCodeIso4217);
					} else {
						log.debug("[Sales Data Import Service] Looking up " + currencyCodeIso4217 + " currency ISO code!");
						Currency currency = currencyRepository.findByIso4217(currencyCodeIso4217);
						currencyToID.put(currencyCodeIso4217, String.valueOf(currency.getCurrencyId()));
						data[4] = String.valueOf(currency.getCurrencyId());
					}

					// TODO: Currency Conversion to SEK before importing to DB

					bufferedWriter.write(data[0] + "\t" + data[1] + "\t" + data[2] + "\t" + data[3] + "\t" + data[4] + "\t" + data[5] + "\n");
				}
				bufferedWriter.close();
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000;
			log.debug("[PROFILING::" + salesData.getSalesDataId() + "] Preprocessing: " + duration + "ms");
		} catch (IOException ex) {
			log.error("[Sales Data Import Service] Preprocessing did not comlete! Returning original sales data file!");
			return file;
		}

		return processedTransactions;
	}

	private void importTransactions(File file, SalesData salesData) {
		log.debug("[Sales Data Import Service] Importing transactions for sales data batch ID " + salesData.getSalesDataId());

		long startTime = System.nanoTime();
		int modifications = salesDataRepository.importSalesDataBatchNoSelect(file.getAbsolutePath().replace("\\", "/"), salesData.getSalesDataId());
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		log.debug("[PROFILING::" + salesData.getSalesDataId() + "] Importing (" + modifications + " records): " + duration + "ms");

		salesData.setStatus(SalesDataStatus.CHECKED);
		salesDataRepository.save(salesData);
	}
}
