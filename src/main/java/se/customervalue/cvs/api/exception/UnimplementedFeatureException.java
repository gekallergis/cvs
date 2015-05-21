package se.customervalue.cvs.api.exception;

public class UnimplementedFeatureException extends Exception {
	public UnimplementedFeatureException() {}

	public UnimplementedFeatureException(String message)
	{
		super(message);
	}
}
