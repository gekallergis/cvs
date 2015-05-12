package se.customervalue.cvs.domain;

public enum ReportStatus
{
	GENERATING("generating"),
	READY("ready"),
	ERROR("error");

	private String descr;

	ReportStatus(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
