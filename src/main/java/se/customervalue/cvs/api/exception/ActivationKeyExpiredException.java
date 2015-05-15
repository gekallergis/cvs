package se.customervalue.cvs.api.exception;

public class ActivationKeyExpiredException extends Exception {
	public ActivationKeyExpiredException() {}

	public ActivationKeyExpiredException(String message)
	{
		super(message);
	}
}
