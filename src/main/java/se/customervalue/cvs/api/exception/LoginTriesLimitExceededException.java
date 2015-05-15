package se.customervalue.cvs.api.exception;

public class LoginTriesLimitExceededException extends Exception {
	public LoginTriesLimitExceededException() {}

	public LoginTriesLimitExceededException(String message)
	{
		super(message);
	}
}
