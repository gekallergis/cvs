package se.customervalue.cvs.api.representation;

public class ManagingEmployeeAttachmentRepresentation {
	private int companyId;

	private int employeeId;

	public ManagingEmployeeAttachmentRepresentation() {}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
}
