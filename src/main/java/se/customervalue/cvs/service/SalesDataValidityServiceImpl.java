package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.SalesDataRepository;
import se.customervalue.cvs.api.exception.SalesDataValidationException;
import se.customervalue.cvs.common.CVSConfig;
import se.customervalue.cvs.domain.SalesData;
import se.customervalue.cvs.domain.SalesDataStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Service
public class SalesDataValidityServiceImpl implements SalesDataValidityService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SalesDataRepository salesDataRepository;

	@Override @Async
	public void startValidation(File file, SalesData salesData) {
		log.debug("[Sales Data Validation Service] Validating file " + file.getAbsolutePath() + " [" + salesData.getSalesDataId() + "]");

		try {
			Instant start = Instant.now();

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
					throw new SalesDataValidationException();
				}

				boolean salesDataFileIsValid = true;
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

				if(salesDataFileIsValid) {
					salesData.setStatus(SalesDataStatus.CHECKED);
					salesDataRepository.save(salesData);
				} else {
					throw new SalesDataValidationException();
				}
			}

			Instant end = Instant.now();
			log.debug("[Sales Data Validation Service] " + file.getName() + " validated in " + Duration.between(start, end).getNano() / 1000000 + "ms");
		} catch (IOException ex) {
			log.error("[Sales Data Validation Service] Something went wrong while validating " + file.getAbsolutePath());
		} catch (ParseException e) {
			log.error("[Sales Data Validation Service] Error parsing date limits for " + file.getAbsolutePath());
		} catch (SalesDataValidationException e) {
			log.error("[Sales Data Validation Service] Error parsing sales data file " + file.getAbsolutePath());
			salesData.setStatus(SalesDataStatus.ERROR);
			salesDataRepository.save(salesData);
		}
	}
}
