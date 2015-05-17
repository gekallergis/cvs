package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.Employee;

import java.util.List;

public class BasicEmployeeRepresentation {
	private int employeeId;

	private String email;

	private String firstName;

	private String lastName;

	private String photoPath;

	private String password;

	private List<RoleRepresentation> roles;

	public BasicEmployeeRepresentation() {}

	public BasicEmployeeRepresentation(Employee employee) {
		this.employeeId = employee.getEmployeeId();
		this.email = employee.getEmail();
		this.firstName = employee.getFirstName();
		this.lastName = employee.getLastName();
		this.photoPath = employee.getPhotoPath();
	}

	public BasicEmployeeRepresentation(int employeeId, String email, String firstName, String lastName, String photoPath) {
		this.employeeId = employeeId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.photoPath = photoPath;
	}

	public List<RoleRepresentation> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleRepresentation> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
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

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
}
