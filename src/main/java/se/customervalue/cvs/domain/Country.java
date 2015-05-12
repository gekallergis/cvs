package se.customervalue.cvs.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Country {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int countryId;

	private String name;

	private String ISO31661a2;

	private String ISO31661a3;

	private String numericCode;

	public Country() {}

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
