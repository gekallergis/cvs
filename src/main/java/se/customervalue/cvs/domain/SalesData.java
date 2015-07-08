package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class SalesData {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int salesDataId;

	private String salesPeriod;

	@Enumerated(EnumType.STRING)
	private SalesDataStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadedOn;

	@Lob
	private String filePath;

	@ManyToOne
	@JoinColumn(name="uploadedFor", foreignKey = @ForeignKey(name = "FK_SalesDataUploadedFor"))
	private Company company;

	@OneToMany(mappedBy="salesData")
	private List<Report> reports = new ArrayList<Report>();

	@ManyToOne
	@JoinColumn(name="uploadedBy", foreignKey = @ForeignKey(name = "FK_SaleDataUploadedBy"))
	private Employee uploader;

	@OneToMany(mappedBy="salesDataBatch", fetch = FetchType.LAZY)
	private List<Transaction> transactions = new ArrayList<Transaction>();

	public SalesData() {}

	public SalesData(String salesPeriod, SalesDataStatus status, Date uploadedOn, String filePath) {
		this.salesPeriod = salesPeriod;
		this.status = status;
		this.uploadedOn = uploadedOn;
		this.filePath = filePath;
	}

	@PrePersist
	protected void onCreate() {
		uploadedOn = new Date();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Employee getUploader() {
		return uploader;
	}

	public void setUploader(Employee uploader) {
		this.uploader = uploader;
	}

	public Company getCompany() {
		return company;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public void setCompany(Company company) {
		this.company = company;
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
