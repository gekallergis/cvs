package se.customervalue.cvs.domain;

import javax.persistence.*;

@Entity
public class Invoice {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int invoiceId;

	private String invoiceNumber;

	@Enumerated(EnumType.STRING)
	private InvoiceStatus stastus;

	public Invoice() {}

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

	public InvoiceStatus getStastus() {
		return stastus;
	}

	public void setStastus(InvoiceStatus stastus) {
		this.stastus = stastus;
	}
}
