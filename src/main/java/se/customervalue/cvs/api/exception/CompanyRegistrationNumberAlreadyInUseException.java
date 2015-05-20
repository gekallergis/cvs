package se.customervalue.cvs.api.exception;

public class CompanyRegistrationNumberAlreadyInUseException extends Exception {
	public CompanyRegistrationNumberAlreadyInUseException() {}

	public CompanyRegistrationNumberAlreadyInUseException(String message)
	{
		super(message);
	}
}
