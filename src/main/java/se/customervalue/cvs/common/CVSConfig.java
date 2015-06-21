package se.customervalue.cvs.common;

public final class CVSConfig  {
	// Maximum login tries allowed
	public static final int LOGIN_MAX_TRIES = 3;

	// Default invoice limit for companies
	public static final float DEFAULT_INVOICE_LIMIT = 100000.0f;

	// Service endpoint
	public static final String SERVICE_ENDPOINT = "https://localhost/#/";

	// Activation key expiration time in minutes.
	public static final long ACTIVATION_KEY_LIFETIME_MINUTES = 120;

	// Default invoice due date in days after purchase.
	public static final int INVOICE_DUE_DATE_DAYS_AFTER_PURCHASE = 15;

	// Default VAT for purchases.
	public static final float DEFAULT_VAT = 15.0f;

	// Default sales data filesystem storage
	public static final String SALES_DATA_FS_STORE = "c:/cvs/salesData";

	// Default reports filesystem storage
	public static final String REPORTS_FS_STORE = "c:/cvs/reports";

	// Default temporary filesystem storage
	public static final String TEMP_FS_STORE = "c:/cvs/temp";

	private CVSConfig(){
		// This prevents instantiation of this class by mistake!
		throw new AssertionError();
	}
}
