package se.customervalue.cvs.service.SalesDataImport;

import org.springframework.scheduling.annotation.Async;
import se.customervalue.cvs.domain.SalesData;

import java.io.File;

public interface SalesDataImportService {
	@Async
	void start(File file, SalesData salesData);
}
