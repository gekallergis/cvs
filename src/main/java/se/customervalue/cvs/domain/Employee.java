package se.customervalue.cvs.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Employee {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employeeId;

	private String email;

	private String firstName;

	private String lastName;

	private String password;

	private String photoPath;

	private boolean isActive;

	@ManyToOne
	@JoinColumn(name="employer")
	private Company employer;

	@ManyToMany
	@JoinTable(name="EmployeeRole", joinColumns = @JoinColumn(name="employeeId"), inverseJoinColumns = @JoinColumn(name="roleId"))
	private Collection<Role> roles = new ArrayList<Role>();

	public Employee() {}

	public Employee(String email, String firstName, String lastName, String password, String photoPath, boolean isActive, Company employer) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.photoPath = photoPath;
		this.isActive = isActive;
		this.employer = employer;
	}

	@PrePersist
	protected void onCreate() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		password = encoder.encode(password);
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
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
