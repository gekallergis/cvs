package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.ReportRepresentation;
import se.customervalue.cvs.service.ReportService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ReportController {
	@Autowired
	private HttpSession session;

	@Autowired
	private ReportService reportService;

	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public List<ReportRepresentation> getReportsEndpoint() throws UnauthenticatedAccess {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return reportService.getReports(currentlyLoggedInEmployee);
	}
}
