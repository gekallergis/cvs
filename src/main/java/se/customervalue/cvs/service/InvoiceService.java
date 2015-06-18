package se.customervalue.cvs.service;

import se.customervalue.cvs.api.exception.InvoiceNotFoundException;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.InvoiceRepresentation;

import java.util.List;

public interface InvoiceService {
	List<InvoiceRepresentation> getInvoices(EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess;
	InvoiceRepresentation getInvoice(int invoiceId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, InvoiceNotFoundException;
	APIResponseRepresentation settleInvoice(int invoiceId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, InvoiceNotFoundException;
}
