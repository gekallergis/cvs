package se.customervalue.cvs.domain;

public enum InvoiceStatus
{
	PAID("paid"),
	REFUND("refund"),
	UNPAID("unpaid");

	private String descr;

	InvoiceStatus(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
