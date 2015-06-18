package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.customervalue.cvs.api.exception.InvoiceNotFoundException;
import se.customervalue.cvs.api.exception.UnauthenticatedAccess;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.InvoiceRepresentation;
import se.customervalue.cvs.service.InvoiceService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class InvoiceController {
	@Autowired
	private HttpSession session;

	@Autowired
	private InvoiceService invoiceService;

	@RequestMapping(value = "/invoice", method = RequestMethod.GET)
	public List<InvoiceRepresentation> getInvoicesEndpoint() throws UnauthenticatedAccess, UnauthorizedResourceAccess {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return invoiceService.getInvoices(currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/invoice/{id}", method = RequestMethod.GET)
	public InvoiceRepresentation getInvoiceEndpoint(@PathVariable("id") int invoiceId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, InvoiceNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return invoiceService.getInvoice(invoiceId, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/invoice/settle/{id}", method = RequestMethod.POST)
	public APIResponseRepresentation settleInvoiceEndpoint(@PathVariable("id") int invoiceId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, InvoiceNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return invoiceService.settleInvoice(invoiceId, currentlyLoggedInEmployee);
	}
}
