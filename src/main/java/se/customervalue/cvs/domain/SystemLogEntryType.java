package se.customervalue.cvs.domain;

import com.fasterxml.jackson.annotation.JsonValue;

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

	@JsonValue
	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
