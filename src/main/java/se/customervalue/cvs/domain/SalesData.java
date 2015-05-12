package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SalesData {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int salesDataId;

	private String salesPeriod;

	@Enumerated(EnumType.STRING)
	private SalesDataStatus status;

	@Lob
	private Date uploadedOn;

	private String filePath;

	public SalesData() {}

	@PrePersist
	protected void onCreate() {
		uploadedOn = new Date();
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

	public SalesDataStatus getStatus() {
		return status;
	}

	public void setStatus(SalesDataStatus status) {
		this.status = status;
	}

	public Date getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
