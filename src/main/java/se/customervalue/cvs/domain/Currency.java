package se.customervalue.cvs.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Currency {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int currencyId;

	private String name;

	private String ISO4217;

	private String numericCode;

	public Currency() {}

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

	public String getISO4217() {
		return ISO4217;
	}

	public void setISO4217(String ISO4217) {
		this.ISO4217 = ISO4217;
	}

	public String getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(String numericCode) {
		this.numericCode = numericCode;
	}
}
