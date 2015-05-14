package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.Currency;

public class CurrencyRepresentation {
	private int currencyId;

	private String name;

	public CurrencyRepresentation() {}

	public CurrencyRepresentation(Currency currency) {
		this.currencyId = currency.getCurrencyId();
		this.name = currency.getName();
	}

	public CurrencyRepresentation(int currencyId, String name) {
		this.currencyId = currencyId;
		this.name = name;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
