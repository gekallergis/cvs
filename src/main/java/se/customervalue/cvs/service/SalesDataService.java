package se.customervalue.cvs.service;

import org.springframework.web.multipart.MultipartFile;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SalesDataRepresentation;

import java.util.List;

public interface SalesDataService {
	List<SalesDataRepresentation> getSalesData(EmployeeRepresentation loggedInEmployee);
	APIResponseRepresentation uploadSalesData(MultipartFile salesData, int companyId, String month, String year, EmployeeRepresentation loggedInEmployee) throws InvalidEmployeeCompanyCombinationException, FoundActiveProcessingException, SalesDataUploadException;
	APIResponseRepresentation deleteSalesData(int salesDataId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, SalesDataDeleteException;
}
