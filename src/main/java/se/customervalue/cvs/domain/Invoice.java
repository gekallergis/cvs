package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Invoice {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int invoiceId;

	private String invoiceNumber;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;

	private float VAT;

	@Enumerated(EnumType.STRING)
	private InvoiceStatus status;

	@OneToOne(mappedBy="invoice")
	private OrderHeader order;

	public Invoice() {}

	public Invoice(String invoiceNumber, Date dueDate, float VAT, InvoiceStatus status) {
		this.invoiceNumber = invoiceNumber;
		this.dueDate = dueDate;
		this.VAT = VAT;
		this.status = status;
	}

	public OrderHeader getOrder() {

		return order;
	}

	public void setOrder(OrderHeader order) {
		this.order = order;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public float getVAT() {
		return VAT;
	}

	public void setVAT(float VAT) {
		this.VAT = VAT;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public InvoiceStatus getStatus() {
		return status;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}
}
