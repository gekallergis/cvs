package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.Company;

public class BasicCompanyRepresentation {
	private int companyId;

	private String registrationNumber;

	private String name;

	private String phoneNumber;

	private String primaryAddress;

	private String secondaryAddress;

	private String postcode;

	private String city;

	private CountryRepresentation country;

	public BasicCompanyRepresentation() {}

	public BasicCompanyRepresentation(Company company) {
		this.companyId = company.getCompanyId();
		this.registrationNumber = company.getRegistrationNumber();
		this.name = company.getName();
		this.phoneNumber = company.getPhoneNumber();
		this.primaryAddress = company.getPrimaryAddress();
		this.secondaryAddress = company.getSecondaryAddress();
		this.postcode = company.getPostcode();
		this.city = company.getCity();
		this.country = new CountryRepresentation(company.getCountry());
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(String primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public String getSecondaryAddress() {
		return secondaryAddress;
	}

	public void setSecondaryAddress(String secondaryAddress) {
		this.secondaryAddress = secondaryAddress;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public CountryRepresentation getCountry() {
		return country;
	}

	public void setCountry(CountryRepresentation country) {
		this.country = country;
	}
}
