package se.customervalue.cvs.service.SalesDataImport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.CountryRepository;
import se.customervalue.cvs.abstraction.dataaccess.CurrencyRepository;
import se.customervalue.cvs.abstraction.dataaccess.SalesDataRepository;
import se.customervalue.cvs.abstraction.dataaccess.TransactionRepository;
import se.customervalue.cvs.common.CVSConfig;
import se.customervalue.cvs.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${spring.jpa.hibernate.jdbc.batch-size}")
	private int batchSize;

	@Override @Async @Transactional
	public void start(File file, SalesData salesData) {
		if(isValid(file, salesData)) {
			importTransactionsFast(file, salesData);
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
			salesData.setStatus(SalesDataStatus.ERROR);
			salesDataRepository.save(salesData);
		}

		return salesDataFileIsValid;
	}

	private void importTransactions(File file, SalesData salesData) {
		log.debug("[Sales Data Validation Service] Importing transactions for sales data batch ID " + salesData.getSalesDataId());
		try {
			long startTime = System.nanoTime();

			int transactionCount = 0;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				String line = bufferedReader.readLine();
				while((line = bufferedReader.readLine()) != null) {
					transactionCount++;
					String[] transactionData = line.split("\t");
					Transaction newTransaction = new Transaction(transactionData[0], dateFormat.parse(transactionData[2]), Float.parseFloat(transactionData[5]));
					newTransaction.setSalesDataBatch(salesData);
					salesData.getTransactions().add(newTransaction);

					Country newTransactionCountry = countryRepository.findByIso31661a2(transactionData[1].substring(1, transactionData[1].length()-1));
					Currency newTransactionCurrency = currencyRepository.findByIso4217(transactionData[4].substring(1, transactionData[4].length()-1));

					newTransaction.setCountry(newTransactionCountry);
					newTransactionCountry.getTransactions().add(newTransaction);
					newTransaction.setCurrency(newTransactionCurrency);
					newTransactionCurrency.getTransactions().add(newTransaction);

					transactionRepository.save(newTransaction);
					countryRepository.save(newTransactionCountry);
					currencyRepository.save(newTransactionCurrency);

					if (transactionCount % batchSize == 0) {
						entityManager.flush();
						entityManager.clear();
					}
				}
			}

			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000;
			log.debug("[Sales Data Validation Service] " + file.getName() + " imported in " + duration + "ms");

			salesData.setStatus(SalesDataStatus.CHECKED);
			salesDataRepository.save(salesData);
		} catch (IOException ex) {
			log.error("[Sales Data Validation Service] Something went wrong while validating " + file.getAbsolutePath());
			salesData.setStatus(SalesDataStatus.ERROR);
			salesDataRepository.save(salesData);
		} catch (ParseException e) {
			log.error("[Sales Data Validation Service] Error parsing date limits for " + file.getAbsolutePath());
			salesData.setStatus(SalesDataStatus.ERROR);
			salesDataRepository.save(salesData);
		}
	}

	private void importTransactionsFast(File file, SalesData salesData) {
		log.debug("[Sales Data Validation Service] Importing transactions for sales data batch ID " + salesData.getSalesDataId());

		long startTime = System.nanoTime();

		String importQueryString = "SET FOREIGN_KEY_CHECKS = 0; SET UNIQUE_CHECKS = 0; SET SESSION tx_isolation='READ-UNCOMMITTED'; " +
								 "LOAD DATA LOCAL INFILE \" ? \" INTO TABLE cvs.transaction " +
								 "FIELDS TERMINATED BY '\\t' " +
								 "OPTIONALLY ENCLOSED BY '\"' " +
								 "LINES TERMINATED BY '\\r\\n' " +
								 "IGNORE 1 LINES " +
								 "(consumerId, @countryId, date, @dummy, @currencyId, amount) " +
								 "SET country = (SELECT countryId FROM country WHERE iso31661a2 = TRIM(BOTH '\"' FROM @countryId)), " +
									 "currency = (SELECT currencyId FROM currency WHERE iso4217 = TRIM(BOTH '\"' FROM @currencyId)), " +
									 "salesDataBatch = ? ; " +
								 "SET UNIQUE_CHECKS = 1; SET FOREIGN_KEY_CHECKS = 1; SET SESSION tx_isolation='REPEATABLE-READ'; ";

		Query importQuery = entityManager.createNativeQuery(importQueryString);
		Query finalImportQuery = importQuery.setParameter(1, salesData.getFilePath().replace("\\", "/")).setParameter(2, salesData.getSalesDataId());
		int modifications = finalImportQuery.executeUpdate();

		log.warn("[Sales Data Validation Service] Imported " + modifications + " records to the database!");

		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		log.debug("[Sales Data Validation Service] " + file.getName() + " imported in " + duration + "ms");

		salesData.setStatus(SalesDataStatus.CHECKED);
		salesDataRepository.save(salesData);
		entityManager.refresh(salesData);
	}
}
