package se.customervalue.cvs.domain;

public enum SystemLogEntryType
{
	REPORT("report"),
	COMPANY("company"),
	ACCOUNT("account"),
	FINANCE("finance"),
	MISC("misc");

	private String descr;

	SystemLogEntryType(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
