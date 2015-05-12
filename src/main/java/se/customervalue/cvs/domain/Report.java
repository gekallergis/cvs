package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Report {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int reportId;

	private Date generatedOn;

	@Lob
	private String filePath;

	@Enumerated(EnumType.STRING)
	private ReportStatus status;

	public Report() {}

	@PrePersist
	protected void onCreate() {
		generatedOn = new Date();
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
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
