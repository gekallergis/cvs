package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Country {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int countryId;

	private String name;

	private String iso31661a2;

	private String iso31661a3;

	private String numericCode;

	@OneToMany(mappedBy="country")
	private List<Company> companies = new ArrayList<Company>();

	@OneToMany(mappedBy="country")
	private List<Transaction> transactions = new ArrayList<Transaction>();

	public Country() {}

	public Country(String name, String iso31661a2, String iso31661a3, String numericCode) {
		this.name = name;
		this.iso31661a2 = iso31661a2;
		this.iso31661a3 = iso31661a3;
		this.numericCode = numericCode;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
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

	public String getIso31661a2() {
		return iso31661a2;
	}

	public void setIso31661a2(String iso31661a2) {
		this.iso31661a2 = iso31661a2;
	}

	public String getIso31661a3() {
		return iso31661a3;
	}

	public void setIso31661a3(String iso31661a3) {
		this.iso31661a3 = iso31661a3;
	}

	public String getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(String numericCode) {
		this.numericCode = numericCode;
	}
}
