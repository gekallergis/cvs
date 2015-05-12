package se.customervalue.cvs.domain;

public enum SalesDataStatus
{
	RECEIVED("received"),
	PROCESSING("processing"),
	CHECKED("checked"),
	ERROR("error");

	private String descr;

	SalesDataStatus(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
