package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Report {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int reportId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date generatedOn;

	@Lob
	private String filePath;

	@Enumerated(EnumType.STRING)
	private ReportStatus status;

	@ManyToOne
	@JoinColumn(name="generatedFor", foreignKey = @ForeignKey(name = "FK_ReportGeneratedFor"))
	private Company company;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_ReportProduct"))
	private Product product;

	@ManyToOne
	@JoinColumn(name="salesData", foreignKey = @ForeignKey(name = "FK_ReportSalesData"))
	private SalesData salesData;

	@ManyToOne
	@JoinColumn(name="generatedBy", foreignKey = @ForeignKey(name = "FK_ReportGeneratedBy"))
	private Employee reporter;

	public Report() {}

	public Report(Date generatedOn, String filePath, ReportStatus status) {
		this.generatedOn = generatedOn;
		this.filePath = filePath;
		this.status = status;
	}

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
