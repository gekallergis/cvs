package se.customervalue.cvs.common;

public final class CVSConfig  {
	// Maximum login tries allowed
	public static final int LOGIN_MAX_TRIES = 3;

	// Default invoice limit for companies
	public static final float DEFAULT_INVOICE_LIMIT = 50000.0f;

	// Service endpoint
	public static final String SERVICE_ENDPOINT = "https://localhost/#/";

	// Activation key expiration time in minutes.
	public static final long ACTIVATION_KEY_LIFETIME_MINUTES = 120;

	private CVSConfig(){
		// This prevents instantiation of this class by mistake!
		throw new AssertionError();
	}
}
