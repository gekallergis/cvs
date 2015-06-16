package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.customervalue.cvs.api.exception.ProductNotFoundException;
import se.customervalue.cvs.api.exception.UnauthenticatedAccess;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.exception.UnpaidInvoiceQuotaReached;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.OrderHeaderRepresentation;
import se.customervalue.cvs.service.OrderService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class OrderController {
	@Autowired
	private HttpSession session;

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/order", method = RequestMethod.PUT)
	public APIResponseRepresentation placeOrderEndpoint(@RequestBody @Valid OrderHeaderRepresentation order) throws UnauthenticatedAccess, UnauthorizedResourceAccess, ProductNotFoundException, UnpaidInvoiceQuotaReached {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return orderService.placeOrder(order, currentlyLoggedInEmployee);
	}
}
