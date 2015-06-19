package se.customervalue.cvs.service;

import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SalesDataRepresentation;

import java.util.List;

public interface SalesDataService {
	List<SalesDataRepresentation> getSalesData(EmployeeRepresentation loggedInEmployee);
}
