package se.customervalue.cvs.api.exception;

public class NotEnoughOwnedProductsException extends Exception {
	public NotEnoughOwnedProductsException() {}

	public NotEnoughOwnedProductsException(String message)
	{
		super(message);
	}
}
