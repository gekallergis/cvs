package se.customervalue.cvs.api.exception;

public class EmployeeNotWorkingForCompanyException extends Exception {
	public EmployeeNotWorkingForCompanyException() {}

	public EmployeeNotWorkingForCompanyException(String message)
	{
		super(message);
	}
}
