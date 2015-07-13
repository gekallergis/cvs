package se.customervalue.cvs.api.exception;

public class UnauthorizedReportGeneration extends Exception {
	public UnauthorizedReportGeneration() {}

	public UnauthorizedReportGeneration(String message)
	{
		super(message);
	}
}
