package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Company {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int companyId;

	private String name;

	private String phoneNumber;

	private String registrationNumber;

	private float invoiceLimit;

	private String city;

	private String primaryAddress;

	private String secondaryAddress;

	private String postcode;

	@OneToOne
	private Employee managingEmployee;

	@OneToMany(mappedBy="employer")
	private Collection<Employee> employees = new ArrayList<Employee>();

	public Company() {}

	public Company(String name, String phoneNumber, String registrationNumber, float invoiceLimit, String city, String primaryAddress, String secondaryAddress, String postcode, Employee managingEmployee) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.registrationNumber = registrationNumber;
		this.invoiceLimit = invoiceLimit;
		this.city = city;
		this.primaryAddress = primaryAddress;
		this.secondaryAddress = secondaryAddress;
		this.postcode = postcode;
		this.managingEmployee = managingEmployee;
	}

	public Collection<Employee> getEmployees() {
		return employees;
	}

	public Employee getManagingEmployee() {
		return managingEmployee;
	}

	public void setManagingEmployee(Employee managingEmployee) {
		this.managingEmployee = managingEmployee;
	}

	public void setEmployees(Collection<Employee> employee) {
		this.employees = employee;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
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

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public float getInvoiceLimit() {
		return invoiceLimit;
	}

	public void setInvoiceLimit(float invoiceLimit) {
		this.invoiceLimit = invoiceLimit;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
}
