package se.customervalue.cvs.service;

import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.OrderHeaderRepresentation;

import java.util.List;

public interface OrderService {
	APIResponseRepresentation placeOrder(OrderHeaderRepresentation order, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, ProductNotFoundException, UnpaidInvoiceQuotaReached;
	List<OrderHeaderRepresentation> getOrders(EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess;
	APIResponseRepresentation refundOrder(int refundOrderId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, NotEnoughOwnedProductsException, OrderNotFoundException;
}
