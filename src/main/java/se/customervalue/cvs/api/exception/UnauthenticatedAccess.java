package se.customervalue.cvs.api.exception;

public class UnauthenticatedAccess extends Exception {
	public UnauthenticatedAccess() {}

	public UnauthenticatedAccess(String message)
	{
		super(message);
	}
}
