package se.customervalue.cvs.api.representation;

import se.customervalue.cvs.api.representation.domain.CompanyRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;

import java.util.List;

public class CompanyHierarchyRepresentation {
	private CompanyRepresentation company;

	private List<EmployeeRepresentation> employees;

	private List<CompanyHierarchyRepresentation> children;

	public CompanyHierarchyRepresentation() {}

	public CompanyRepresentation getCompany() {
		return company;
	}

	public void setCompany(CompanyRepresentation company) {
		this.company = company;
	}

	public List<EmployeeRepresentation> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeRepresentation> employees) {
		this.employees = employees;
	}

	public List<CompanyHierarchyRepresentation> getChildren() {
		return children;
	}

	public void setChildren(List<CompanyHierarchyRepresentation> children) {
		this.children = children;
	}
}
