package se.customervalue.cvs.domain;

public enum SalesDataStatus
{
	PROCESSING("processing"),
	CHECKED("checked"),
	ERROR("error"),
	REPLACED("replaced"),
	DELETED("deleted");

	private String descr;

	SalesDataStatus(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
