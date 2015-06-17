package se.customervalue.cvs.api.exception;

public class OrderNotFoundException extends Exception {
	public OrderNotFoundException() {}

	public OrderNotFoundException(String message)
	{
		super(message);
	}
}
