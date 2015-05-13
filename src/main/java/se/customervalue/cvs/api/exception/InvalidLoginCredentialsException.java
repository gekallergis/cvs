package se.customervalue.cvs.api.exception;

public class InvalidLoginCredentialsException extends Exception
{
	public InvalidLoginCredentialsException() {}

	public InvalidLoginCredentialsException(String message)
	{
		super(message);
	}
}
