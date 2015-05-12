package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Country {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int countryId;

	private String name;

	private String ISO31661a2;

	private String ISO31661a3;

	private String numericCode;

	@OneToMany(mappedBy="country")
	private Collection<Company> companies = new ArrayList<Company>();

	@OneToMany(mappedBy="country")
	private Collection<Transaction> transactions = new ArrayList<Transaction>();

	public Country() {}

	public Collection<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Collection<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Collection<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(Collection<Company> companies) {
		this.companies = companies;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getISO31661a2() {
		return ISO31661a2;
	}

	public void setISO31661a2(String ISO31661a2) {
		this.ISO31661a2 = ISO31661a2;
	}

	public String getISO31661a3() {
		return ISO31661a3;
	}

	public void setISO31661a3(String ISO31661a3) {
		this.ISO31661a3 = ISO31661a3;
	}

	public String getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(String numericCode) {
		this.numericCode = numericCode;
	}
}
