package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Currency {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int currencyId;

	private String name;

	private String iso4217;

	private String numericCode;

	@OneToMany(mappedBy="currency")
	private List<Transaction> transactions = new ArrayList<Transaction>();

	public Currency() {}

	public Currency(String name, String iso4217, String numericCode) {
		this.name = name;
		this.iso4217 = iso4217;
		this.numericCode = numericCode;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
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

	public String getIso4217() {
		return iso4217;
	}

	public void setIso4217(String iso4217) {
		this.iso4217 = iso4217;
	}

	public String getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(String numericCode) {
		this.numericCode = numericCode;
	}
}
