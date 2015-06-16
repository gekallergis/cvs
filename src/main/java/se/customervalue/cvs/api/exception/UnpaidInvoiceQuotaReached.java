package se.customervalue.cvs.api.exception;

public class UnpaidInvoiceQuotaReached extends Exception {
	public UnpaidInvoiceQuotaReached() {}

	public UnpaidInvoiceQuotaReached(String message)
	{
		super(message);
	}
}
