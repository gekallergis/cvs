package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.SalesData;
import se.customervalue.cvs.domain.SalesDataStatus;

import java.util.Date;

public class SalesDataRepresentation {
	private int salesDataId;

	private String salesPeriod;

	private Date uploadedOn;

	private BasicEmployeeRepresentation uploadedBy;

	private BasicCompanyRepresentation uploadedFor;

	private String filePath;

	private SalesDataStatus status;

	public SalesDataRepresentation() {}

	public SalesDataRepresentation(SalesData salesData) {
		this.salesDataId = salesData.getSalesDataId();
		this.salesPeriod = salesData.getSalesPeriod();
		this.uploadedOn = salesData.getUploadedOn();
		this.uploadedBy = new BasicEmployeeRepresentation(salesData.getUploader());
		this.uploadedFor = new BasicCompanyRepresentation(salesData.getCompany());
		this.filePath = salesData.getFilePath();
		this.status = salesData.getStatus();
	}

	public SalesDataRepresentation(int salesDataId, String salesPeriod, Date uploadedOn, String filePath, SalesDataStatus status) {
		this.salesDataId = salesDataId;
		this.salesPeriod = salesPeriod;
		this.uploadedOn = uploadedOn;
		this.filePath = filePath;
		this.status = status;
	}

	public int getSalesDataId() {
		return salesDataId;
	}

	public void setSalesDataId(int salesDataId) {
		this.salesDataId = salesDataId;
	}

	public String getSalesPeriod() {
		return salesPeriod;
	}

	public void setSalesPeriod(String salesPeriod) {
		this.salesPeriod = salesPeriod;
	}

	public Date getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public BasicEmployeeRepresentation getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(BasicEmployeeRepresentation uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public BasicCompanyRepresentation getUploadedFor() {
		return uploadedFor;
	}

	public void setUploadedFor(BasicCompanyRepresentation uploadedFor) {
		this.uploadedFor = uploadedFor;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public SalesDataStatus getStatus() {
		return status;
	}

	public void setStatus(SalesDataStatus status) {
		this.status = status;
	}
}
