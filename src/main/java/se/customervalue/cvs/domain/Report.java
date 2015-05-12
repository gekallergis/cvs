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

	@ManyToOne
	@JoinColumn(name="generatedFor")
	private Company company;

	@OneToOne
	private Product product;

	@ManyToOne
	@JoinColumn(name="salesData")
	private SalesData salesData;

	@ManyToOne
	@JoinColumn(name="generatedBy")
	private Employee reporter;

	public Report() {}

	@PrePersist
	protected void onCreate() {
		generatedOn = new Date();
	}

	public Employee getReporter() {
		return reporter;
	}

	public void setReporter(Employee reporter) {
		this.reporter = reporter;
	}

	public SalesData getSalesData() {
		return salesData;
	}

	public void setSalesData(SalesData salesData) {
		this.salesData = salesData;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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
