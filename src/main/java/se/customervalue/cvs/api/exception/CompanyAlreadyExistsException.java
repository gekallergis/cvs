package se.customervalue.cvs.api.exception;

public class CompanyAlreadyExistsException extends Exception {
	public CompanyAlreadyExistsException() {}

	public CompanyAlreadyExistsException(String message)
	{
		super(message);
	}
}
