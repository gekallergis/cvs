package se.customervalue.cvs.api.exception;

public class InvalidEmployeeCompanyCombinationException extends Exception {
	public InvalidEmployeeCompanyCombinationException() {}

	public InvalidEmployeeCompanyCombinationException(String message)
	{
		super(message);
	}
}
