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

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
			importTransactions(salesData);
		} else {
			log.error("[Sales Data Validation Service] Error validating sales data file " + file.getAbsolutePath());
			salesData.setStatus(SalesDataStatus.ERROR);
			salesDataRepository.save(salesData);
		}
	}

	private boolean isValid(File file, SalesData salesData) {
		log.debug("[Sales Data Validation Service] Validating file " + file.getAbsolutePath());

		boolean salesDataFileIsValid = true;
		try {
			long startTime = System.nanoTime();

			// File Validation Process
			String edgeID = "\\d{1,20}";
			String country = "\"[A-Z]{1,3}\"";
			String ordDate = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
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
					salesDataFileIsValid = false;
				}

				int lineNumber = 1;
				while((line = bufferedReader.readLine()) != null) {
					lineNumber++;
					if(line.matches(edgeID + "\\t" + country + "\\t" + ordDate + "\\t" + ordYear + "\\t" + currency + "\\t" + amount)) {
						String[] data = line.split("\t");
						Date transactionDate = dateFormat.parse(data[2]);
						if(transactionDate.before(earliestAllowedDate) || transactionDate.after(latestAllowedDate)) {
							log.error("[Sales Data Validation Service] Invalid date on line " + lineNumber);
							salesDataFileIsValid = false;
							break;
						}
					} else {
						log.error("[Sales Data Validation Service] Invalid transaction format on line " + lineNumber);
						salesDataFileIsValid = false;
						break;
					}
				}
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000;
			log.debug("[Sales Data Validation Service] " + file.getName() + " validated in " + duration + "ms");
		} catch (IOException | ParseException ex) {
			salesDataFileIsValid = false;
			salesData.setStatus(SalesDataStatus.ERROR);
			salesDataRepository.save(salesData);
		}

		return salesDataFileIsValid;
	}

	private void importTransactions(SalesData salesData) {
		log.debug("[Sales Data Validation Service] Importing transactions for sales data batch ID " + salesData.getSalesDataId());

		long startTime = System.nanoTime();
		int modifications = salesDataRepository.importSalesDataBatch(salesData.getFilePath().replace("\\", "/"), salesData.getSalesDataId());
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		log.debug("[Sales Data Validation Service] Imported " + modifications + " records to the database in " + duration + "ms");

		salesData.setStatus(SalesDataStatus.CHECKED);
		salesDataRepository.save(salesData);
	}
}
