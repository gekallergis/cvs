package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Currency {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int currencyId;

	private String name;

	private String ISO4217;

	private String numericCode;

	@OneToMany(mappedBy="currency")
	private Collection<Transaction> transactions = new ArrayList<Transaction>();

	public Currency() {}

	public Collection<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Collection<Transaction> transactions) {
		this.transactions = transactions;
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
