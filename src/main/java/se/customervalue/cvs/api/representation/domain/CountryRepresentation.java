package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.Country;

public class CountryRepresentation {
	private int countryId;

	private String name;

	public CountryRepresentation() {}

	public CountryRepresentation(Country country) {
		this.countryId = country.getCountryId();
		this.name = country.getName();
	}

	public CountryRepresentation(int countryId, String name) {
		this.countryId = countryId;
		this.name = name;
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
}
