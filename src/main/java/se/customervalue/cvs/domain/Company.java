package se.customervalue.cvs.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import se.customervalue.cvs.api.representation.CompanyRegistrationInfoRepresentation;
import se.customervalue.cvs.common.CVSConfig;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int companyId;

	private String name;

	private String phoneNumber;

	@Column(unique = true)
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
	private List<Company> subsidiaries = new ArrayList<Company>();

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_CompanyManagingEmployee"))
	private Employee managingEmployee;

	@OneToMany(mappedBy="employer")
	private List<Employee> employees = new ArrayList<Employee>();

	@OneToMany(mappedBy="purchasedFor")
	private List<OrderHeader> orders = new ArrayList<OrderHeader>();

	@OneToMany(mappedBy="owner")
	private List<OwnedProduct> ownedProducts = new ArrayList<OwnedProduct>();

	@OneToMany(mappedBy="company")
	private List<Report> reports = new ArrayList<Report>();

	@ManyToOne
	@JoinColumn(name="country",  foreignKey = @ForeignKey(name = "FK_CompanyCountry"))
	private Country country;

	@OneToMany(mappedBy="company")
	private List<SalesData> salesData = new ArrayList<SalesData>();

	public Company() {}

	public Company(CompanyRegistrationInfoRepresentation companyInfo) {
		this.registrationNumber = companyInfo.getRegistrationNumber();
		this.name = companyInfo.getName();
		this.primaryAddress = companyInfo.getPrimaryAddress();
		this.secondaryAddress = companyInfo.getSecondaryAddress();
		this.postcode = companyInfo.getPostcode();
		this.city = companyInfo.getCity();
		this.phoneNumber = companyInfo.getPhoneNumber();
	}

	public Company(String name, String phoneNumber, String registrationNumber, String city, String primaryAddress, String secondaryAddress, String postcode) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.registrationNumber = registrationNumber;
		this.city = city;
		this.primaryAddress = primaryAddress;
		this.secondaryAddress = secondaryAddress;
		this.postcode = postcode;
	}

	@PrePersist
	protected void onCreate() {
		this.invoiceLimit = CVSConfig.DEFAULT_INVOICE_LIMIT;
	}

	public boolean hasParentCompany() {
		return (this.parentCompany != null);
	}

	public boolean hasManagingEmployee() {
		return (this.managingEmployee != null);
	}

	public List<SalesData> getSalesData() {
		return salesData;
	}

	public void setSalesData(List<SalesData> salesData) {
		this.salesData = salesData;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public List<OwnedProduct> getOwnedProducts() {
		return ownedProducts;
	}

	public void setOwnedProducts(List<OwnedProduct> ownedProducts) {
		this.ownedProducts = ownedProducts;
	}

	public Company getParentCompany() {
		return parentCompany;
	}

	public void setParentCompany(Company parentCompany) {
		this.parentCompany = parentCompany;
	}

	public List<Company> getSubsidiaries() {
		return subsidiaries;
	}

	public void setSubsidiaries(List<Company> subsidiaries) {
		this.subsidiaries = subsidiaries;
	}

	public List<OrderHeader> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderHeader> orders) {
		this.orders = orders;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public Employee getManagingEmployee() {
		return managingEmployee;
	}

	public void setManagingEmployee(Employee managingEmployee) {
		this.managingEmployee = managingEmployee;
	}

	public void setEmployees(List<Employee> employee) {
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
