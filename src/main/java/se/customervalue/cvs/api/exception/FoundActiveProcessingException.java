package se.customervalue.cvs.api.exception;

public class FoundActiveProcessingException extends Exception {
	public FoundActiveProcessingException() {}

	public FoundActiveProcessingException(String message)
	{
		super(message);
	}
}
