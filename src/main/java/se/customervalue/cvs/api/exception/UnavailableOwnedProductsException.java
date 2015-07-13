package se.customervalue.cvs.api.exception;

public class UnavailableOwnedProductsException extends Exception {
	public UnavailableOwnedProductsException() {}

	public UnavailableOwnedProductsException(String message)
	{
		super(message);
	}
}
