package se.customervalue.cvs.service;

import org.springframework.web.multipart.MultipartFile;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SalesDataRepresentation;

import java.util.List;

public interface SalesDataService {
	List<SalesDataRepresentation> getSalesData(EmployeeRepresentation loggedInEmployee);
	APIResponseRepresentation uploadSalesData(MultipartFile salesData, int companyId, String year, String month, EmployeeRepresentation loggedInEmployee);
}
