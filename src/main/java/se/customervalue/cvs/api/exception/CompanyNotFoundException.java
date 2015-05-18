package se.customervalue.cvs.api.exception;

public class CompanyNotFoundException extends Exception {
	public CompanyNotFoundException() {}

	public CompanyNotFoundException(String message)
	{
		super(message);
	}
}
