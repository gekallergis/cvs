package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SalesDataRepresentation;
import se.customervalue.cvs.service.SalesDataService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class SalesDataController {
	@Autowired
	private HttpSession session;

	@Autowired
	private SalesDataService salesDataService;

	@RequestMapping(value = "/salesdata", method = RequestMethod.GET)
	public List<SalesDataRepresentation> getSalesDataEndpoint() throws UnauthenticatedAccess {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return salesDataService.getSalesData(currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/salesdata", method = RequestMethod.POST)
	public APIResponseRepresentation uploadSalesDataEndpoint(@RequestParam("salesData") MultipartFile salesData, @RequestParam("uploadFor") int companyId, @RequestParam("periodMonth") String month, @RequestParam("periodYear") String year) throws UnauthenticatedAccess, InvalidEmployeeCompanyCombinationException, FoundActiveProcessingException, SalesDataUploadException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return salesDataService.uploadSalesData(salesData, companyId, month, year, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/salesdata/{id}", method = RequestMethod.DELETE)
	public APIResponseRepresentation deleteSalesDataEndpoint(@PathVariable("id") int salesDataId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, SalesDataDeleteException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return salesDataService.deleteSalesData(salesDataId, currentlyLoggedInEmployee);
	}
}
