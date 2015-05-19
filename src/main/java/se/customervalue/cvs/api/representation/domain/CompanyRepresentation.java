package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.api.representation.CompanyHierarchyRepresentation;
import se.customervalue.cvs.domain.Company;

import java.io.Serializable;

public class CompanyRepresentation implements Serializable{
	private int companyId;

	private String registrationNumber;

	private String name;

	private String phoneNumber;

	private float invoiceLimit;

	private String primaryAddress;

	private String secondaryAddress;

	private String postcode;

	private String city;

	private CountryRepresentation country;

	private CompanyRepresentation parentCompany;

	private BasicEmployeeRepresentation managingEmployee;

	private CompanyHierarchyRepresentation hierarchy;

	public CompanyRepresentation() {}

	public CompanyRepresentation(Company company) {
		this.companyId = company.getCompanyId();
		this.registrationNumber = company.getRegistrationNumber();
		this.name = company.getName();
		this.phoneNumber = company.getPhoneNumber();
		this.invoiceLimit = company.getInvoiceLimit();
		this.primaryAddress = company.getPrimaryAddress();
		this.secondaryAddress = company.getSecondaryAddress();
		this.postcode = company.getPostcode();
		this.city = company.getCity();
		this.country = new CountryRepresentation(company.getCountry());
		if (company.hasParentCompany()) {
			this.parentCompany = new CompanyRepresentation(company.getParentCompany());
		}
		if (company.hasManagingEmployee()) {
			this.managingEmployee = new BasicEmployeeRepresentation(company.getManagingEmployee());
		}
	}

	public CompanyRepresentation(int companyId, String registrationNumber, String name, String phoneNumber, float invoiceLimit, String primaryAddress, String secondaryAddress, String postcode, String city) {
		this.companyId = companyId;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.invoiceLimit = invoiceLimit;
		this.primaryAddress = primaryAddress;
		this.secondaryAddress = secondaryAddress;
		this.postcode = postcode;
		this.city = city;
	}

	public CompanyHierarchyRepresentation getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(CompanyHierarchyRepresentation hierarchy) {
		this.hierarchy = hierarchy;
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

	public float getInvoiceLimit() {
		return invoiceLimit;
	}

	public void setInvoiceLimit(float invoiceLimit) {
		this.invoiceLimit = invoiceLimit;
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

	public CountryRepresentation getCountry() {	return country; }

	public void setCountry(CountryRepresentation country) {
		this.country = country;
	}

	public CompanyRepresentation getParentCompany() {
		return parentCompany;
	}

	public void setParentCompany(CompanyRepresentation parentCompany) {
		this.parentCompany = parentCompany;
	}

	public BasicEmployeeRepresentation getManagingEmployee() {
		return managingEmployee;
	}

	public void setManagingEmployee(BasicEmployeeRepresentation managingEmployee) {
		this.managingEmployee = managingEmployee;
	}
}
