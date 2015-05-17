package se.customervalue.cvs.api.exception;

public class EmployeeEmailAlreadyInUseException extends Exception {
	public EmployeeEmailAlreadyInUseException() {}

	public EmployeeEmailAlreadyInUseException(String message)
	{
		super(message);
	}
}
