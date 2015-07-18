package se.customervalue.cvs.service.SalesDataImport;

import org.springframework.scheduling.annotation.Async;

import java.io.File;

public interface SalesDataImportService {
	@Async
	void start(File file, int salesDataId);
}
