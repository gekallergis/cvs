package se.customervalue.cvs.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import se.customervalue.cvs.api.representation.EmployeeRegistrationInfoRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.common.CVSConfig;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employeeId;

	@Column(unique = true)
	private String email;

	private String firstName;

	private String lastName;

	private String password;

	private String photoPath;

	private boolean isActive;

	private int loginTries;

	@OneToMany(mappedBy="purchasedBy")
	private List<OrderHeader> orders = new ArrayList<OrderHeader>();

	@ManyToOne
	@JoinColumn(name="employer", foreignKey = @ForeignKey(name = "FK_EmployeeEmployer"))
	private Company employer;

	@ManyToMany
	@JoinTable(name="EmployeeRole", joinColumns = @JoinColumn(name="employeeId"), inverseJoinColumns = @JoinColumn(name="roleId"))
	private List<Role> roles = new ArrayList<Role>();

	@OneToMany(mappedBy="reporter")
	private List<Report> generatedReports = new ArrayList<Report>();

	@OneToMany(mappedBy="uploader")
	private List<SalesData> salesDataUploads = new ArrayList<SalesData>();

	public Employee() {}

	public Employee(EmployeeRegistrationInfoRepresentation employee) {
		this.email = employee.getEmail();
		this.firstName = employee.getFirstName();
		this.lastName = employee.getLastName();
		this.password = employee.getPassword();
		this.isActive = false;
	}

	public Employee(String email, String firstName, String lastName, String password, String photoPath, boolean isActive) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.photoPath = photoPath;
		this.isActive = isActive;
	}

	@PrePersist
	protected void onCreate() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		this.password = encoder.encode(password);
		this.loginTries = CVSConfig.LOGIN_MAX_TRIES;
		this.photoPath = "assets/img/avatars/male-big.png";
	}

	public int getLoginTries() {
		return loginTries;
	}

	public void setLoginTries(int loginTries) {
		this.loginTries = loginTries;
	}

	public List<Report> getGeneratedReports() {
		return generatedReports;
	}

	public void setGeneratedReports(List<Report> generatedReports) {
		this.generatedReports = generatedReports;
	}

	public List<SalesData> getSalesDataUploads() {
		return salesDataUploads;
	}

	public void setSalesDataUploads(List<SalesData> salesDataUploads) {
		this.salesDataUploads = salesDataUploads;
	}

	public List<OrderHeader> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderHeader> orders) {
		this.orders = orders;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int accountID) {
		this.employeeId = accountID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Company getEmployer() {
		return employer;
	}

	public void setEmployer(Company employer) {
		this.employer = employer;
	}
}
