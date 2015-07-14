package se.customervalue.cvs.service;

import org.springframework.scheduling.annotation.Async;
import se.customervalue.cvs.domain.SalesData;

import java.io.File;

public interface SalesDataValidityService {
	@Async
	void startValidation(File file, SalesData salesData);
}
