package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.Report;
import se.customervalue.cvs.domain.ReportStatus;

import java.util.Date;

public class ReportRepresentation {
	private int reportId;

	private String productName;

	private String salesPeriod;

	private String generatedBy;

	private String generatedFor;

	private Date generatedOn;

	private String filePath;

	private ReportStatus status;

	public ReportRepresentation() {}

	public ReportRepresentation(Report report) {
		this.reportId = report.getReportId();
		this.productName = report.getProduct().getName();
		this.salesPeriod = report.getSalesData().getSalesPeriod();
		this.generatedBy = report.getReporter().getFirstName() + " " + report.getReporter().getLastName();
		this.generatedFor = report.getCompany().getName();
		this.generatedOn = report.getGeneratedOn();
		this.filePath = report.getFilePath();
		this.status = report.getStatus();
	}

	public ReportRepresentation(int reportId, String productName, String salesPeriod, String generatedBy, String generatedFor, Date generatedOn, String filePath, ReportStatus status) {
		this.reportId = reportId;
		this.productName = productName;
		this.salesPeriod = salesPeriod;
		this.generatedBy = generatedBy;
		this.generatedFor = generatedFor;
		this.generatedOn = generatedOn;
		this.filePath = filePath;
		this.status = status;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSalesPeriod() {
		return salesPeriod;
	}

	public void setSalesPeriod(String salesPeriod) {
		this.salesPeriod = salesPeriod;
	}

	public String getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	public String getGeneratedFor() {
		return generatedFor;
	}

	public void setGeneratedFor(String generatedFor) {
		this.generatedFor = generatedFor;
	}

	public Date getGeneratedOn() {
		return generatedOn;
	}

	public void setGeneratedOn(Date generatedOn) {
		this.generatedOn = generatedOn;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}
}
