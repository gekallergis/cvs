package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.OrderHeaderRepresentation;
import se.customervalue.cvs.service.OrderService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {
	@Autowired
	private HttpSession session;

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public List<OrderHeaderRepresentation> getOrdersEndpoint() throws UnauthenticatedAccess, UnauthorizedResourceAccess {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return orderService.getOrders(currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/order", method = RequestMethod.PUT)
	public APIResponseRepresentation placeOrderEndpoint(@RequestBody @Valid OrderHeaderRepresentation order) throws UnauthenticatedAccess, UnauthorizedResourceAccess, ProductNotFoundException, UnpaidInvoiceQuotaReached {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return orderService.placeOrder(order, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/refund/{id}", method = RequestMethod.POST)
	public APIResponseRepresentation refundOrderEndpoint(@PathVariable("id") int refundOrderId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, NotEnoughOwnedProductsException, OrderNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return orderService.refundOrder(refundOrderId, currentlyLoggedInEmployee);
	}
}
