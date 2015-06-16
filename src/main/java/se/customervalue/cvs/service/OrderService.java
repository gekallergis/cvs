package se.customervalue.cvs.service;

import se.customervalue.cvs.api.exception.ProductNotFoundException;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.exception.UnpaidInvoiceQuotaReached;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.OrderHeaderRepresentation;

public interface OrderService {
	APIResponseRepresentation placeOrder(OrderHeaderRepresentation order, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, ProductNotFoundException, UnpaidInvoiceQuotaReached;
}
