package se.customervalue.cvs.api.representation;

public class CompanyRegistrationInfoRepresentation {
	private String registrationNumber;

	private String name;

	private String primaryAddress;

	private String secondaryAddress;

	private String postcode;

	private String city;

	private int countryId;

	private String phoneNumber;

	public CompanyRegistrationInfoRepresentation() {}

	public CompanyRegistrationInfoRepresentation(String registrationNumber, String name, String primaryAddress, String secondaryAddress, String postcode, String city, int countryId, String phoneNumber) {
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.primaryAddress = primaryAddress;
		this.secondaryAddress = secondaryAddress;
		this.postcode = postcode;
		this.city = city;
		this.countryId = countryId;
		this.phoneNumber = phoneNumber;
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

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
