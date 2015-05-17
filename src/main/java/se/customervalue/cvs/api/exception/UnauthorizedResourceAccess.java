package se.customervalue.cvs.api.exception;

public class UnauthorizedResourceAccess extends Exception {
	public UnauthorizedResourceAccess() {}

	public UnauthorizedResourceAccess(String message)
	{
		super(message);
	}
}
