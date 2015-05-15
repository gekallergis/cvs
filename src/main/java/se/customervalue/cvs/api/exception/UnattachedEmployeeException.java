package se.customervalue.cvs.api.exception;

public class UnattachedEmployeeException extends Exception {
	public UnattachedEmployeeException() {}

	public UnattachedEmployeeException(String message)
	{
		super(message);
	}
}
