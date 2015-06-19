package se.customervalue.cvs.service;

import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.ReportRepresentation;

import java.util.List;

public interface ReportService {
	List<ReportRepresentation> getReports(EmployeeRepresentation loggedInEmployee);
}
