package se.customervalue.cvs.api.exception;

public class UnsupportedCompanyHierarchyLevelException extends Exception {
	public UnsupportedCompanyHierarchyLevelException() {}

	public UnsupportedCompanyHierarchyLevelException(String message)
	{
		super(message);
	}
}
