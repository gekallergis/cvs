package se.customervalue.cvs.api.representation.domain;

import java.util.List;

public class EmployeeRepresentation {
	private int employeeId;

	private String email;

	private String firstName;

	private String lastName;

	private String photoPath;

	private boolean isActive;

	private List<RoleRepresentation> roles;

	private CompanyRepresentation employer;

	public EmployeeRepresentation() {}

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

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<RoleRepresentation> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleRepresentation> roles) {
		this.roles = roles;
	}

	public CompanyRepresentation getEmployer() {
		return employer;
	}

	public void setEmployer(CompanyRepresentation employer) {
		this.employer = employer;
	}
}
