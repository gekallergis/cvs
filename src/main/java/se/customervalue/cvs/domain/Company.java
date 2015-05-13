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

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_CompanySubsidiaries"))
	private Company parentCompany;

	@OneToMany(mappedBy = "parentCompany", fetch = FetchType.LAZY)
	private Collection<Company> subsidiaries = new ArrayList<Company>();

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_CompanyManagingEmployee"))
	private Employee managingEmployee;

	@OneToMany(mappedBy="employer")
	private Collection<Employee> employees = new ArrayList<Employee>();

	@OneToMany(mappedBy="purchasedFor")
	private Collection<OrderHeader> orders = new ArrayList<OrderHeader>();

	@OneToMany(mappedBy="owner")
	private Collection<OwnedProduct> ownedProducts = new ArrayList<OwnedProduct>();

	@OneToMany(mappedBy="company")
	private Collection<Report> reports = new ArrayList<Report>();

	@ManyToOne
	@JoinColumn(name="country",  foreignKey = @ForeignKey(name = "FK_CompanyCountry"))
	private Country country;

	@OneToMany(mappedBy="company")
	private Collection<SalesData> salesData = new ArrayList<SalesData>();

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

	public Collection<SalesData> getSalesData() {
		return salesData;
	}

	public void setSalesData(Collection<SalesData> salesData) {
		this.salesData = salesData;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Collection<Report> getReports() {
		return reports;
	}

	public void setReports(Collection<Report> reports) {
		this.reports = reports;
	}

	public Collection<OwnedProduct> getOwnedProducts() {
		return ownedProducts;
	}

	public void setOwnedProducts(Collection<OwnedProduct> ownedProducts) {
		this.ownedProducts = ownedProducts;
	}

	public Company getParentCompany() {
		return parentCompany;
	}

	public void setParentCompany(Company parentCompany) {
		this.parentCompany = parentCompany;
	}

	public Collection<Company> getSubsidiaries() {
		return subsidiaries;
	}

	public void setSubsidiaries(Collection<Company> subsidiaries) {
		this.subsidiaries = subsidiaries;
	}

	public Collection<OrderHeader> getOrders() {
		return orders;
	}

	public void setOrders(Collection<OrderHeader> orders) {
		this.orders = orders;
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
