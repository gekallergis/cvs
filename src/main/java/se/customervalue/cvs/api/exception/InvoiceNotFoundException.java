package se.customervalue.cvs.api.exception;

public class InvoiceNotFoundException extends Exception {
	public InvoiceNotFoundException() {}

	public InvoiceNotFoundException(String message)
	{
		super(message);
	}
}
