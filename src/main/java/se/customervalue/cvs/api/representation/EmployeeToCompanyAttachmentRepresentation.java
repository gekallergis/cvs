package se.customervalue.cvs.api.representation;

public class EmployeeToCompanyAttachmentRepresentation {
	private int employeeId;

	private int companyId;

	public EmployeeToCompanyAttachmentRepresentation() {}

	public EmployeeToCompanyAttachmentRepresentation(int employeeId, int companyId) {
		this.employeeId = employeeId;
		this.companyId = companyId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}
