package se.customervalue.cvs.api.representation;

public class RegistrationInfoRepresentation {
	private CompanyRegistrationInfoRepresentation companyInfo;

	private EmployeeRegistrationInfoRepresentation employeeInfo;

	public RegistrationInfoRepresentation() {}

	public RegistrationInfoRepresentation(CompanyRegistrationInfoRepresentation companyInfo, EmployeeRegistrationInfoRepresentation employeeInfo) {
		this.companyInfo = companyInfo;
		this.employeeInfo = employeeInfo;
	}

	public CompanyRegistrationInfoRepresentation getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(CompanyRegistrationInfoRepresentation companyInfo) {
		this.companyInfo = companyInfo;
	}

	public EmployeeRegistrationInfoRepresentation getEmployeeInfo() {
		return employeeInfo;
	}

	public void setEmployeeInfo(EmployeeRegistrationInfoRepresentation employeeInfo) {
		this.employeeInfo = employeeInfo;
	}
}
