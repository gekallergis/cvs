package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.customervalue.cvs.api.exception.UnauthenticatedAccess;
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
}
