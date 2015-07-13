package se.customervalue.cvs.service;

import se.customervalue.cvs.api.exception.ReportGenerationException;
import se.customervalue.cvs.api.exception.UnauthorizedReportGeneration;
import se.customervalue.cvs.api.exception.UnavailableOwnedProductsException;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.GenerateReportRequestRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.ReportRepresentation;

import java.util.List;

public interface ReportService {
	List<ReportRepresentation> getReports(EmployeeRepresentation loggedInEmployee);
	APIResponseRepresentation generateReport(GenerateReportRequestRepresentation report, EmployeeRepresentation loggedInEmployee) throws UnauthorizedReportGeneration, UnavailableOwnedProductsException, ReportGenerationException;
}
