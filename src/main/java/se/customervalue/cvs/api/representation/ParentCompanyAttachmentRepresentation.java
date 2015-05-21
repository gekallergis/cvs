package se.customervalue.cvs.api.representation;

public class ParentCompanyAttachmentRepresentation {
	private int companyId;

	private int parentCompanyId;

	public ParentCompanyAttachmentRepresentation() {}

	public int getParentCompanyId() {
		return parentCompanyId;
	}

	public void setParentCompanyId(int parentCompanyId) {
		this.parentCompanyId = parentCompanyId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}
