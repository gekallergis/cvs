package se.customervalue.cvs.domain;

public enum ProductType
{
	NEWBIZ("NewBiz"),
	PREDICTIVE("PredictiveReport");

	private String descr;

	ProductType(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
