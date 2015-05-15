package se.customervalue.cvs.api.exception;

public class UnauthorizedAccess extends Exception {
	public UnauthorizedAccess() {}

	public UnauthorizedAccess(String message)
	{
		super(message);
	}
}
