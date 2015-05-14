package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.Invoice;
import se.customervalue.cvs.domain.InvoiceStatus;
import se.customervalue.cvs.domain.OrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceRepresentation {
	private int invoiceId;

	private String invoiceNumber;

	private Date dueDate;

	private BasicCompanyRepresentation company;

	private BasicEmployeeRepresentation purchaser;

	private List<OrderItemRepresentation> items;

	private float VAT;

	private InvoiceStatus status;

	public InvoiceRepresentation() {}

	public InvoiceRepresentation(Invoice invoice) {
		this.invoiceId = invoice.getInvoiceId();
		this.invoiceNumber = invoice.getInvoiceNumber();
		this.dueDate = invoice.getDueDate();
		this.company = new BasicCompanyRepresentation(invoice.getOrder().getPurchasedFor());
		this.purchaser = new BasicEmployeeRepresentation(invoice.getOrder().getPurchasedBy());

		List<OrderItemRepresentation> orderItemList = new ArrayList<OrderItemRepresentation>();
		for (OrderItem orderItem : invoice.getOrder().getOrderItems()) {
			orderItemList.add(new OrderItemRepresentation(orderItem));
		}
		this.items = orderItemList;

		this.VAT = invoice.getVAT();
		this.status = invoice.getStatus();
	}

	public InvoiceRepresentation(float VAT, InvoiceStatus status, int invoiceId, String invoiceNumber, Date dueDate) {
		this.VAT = VAT;
		this.status = status;
		this.invoiceId = invoiceId;
		this.invoiceNumber = invoiceNumber;
		this.dueDate = dueDate;
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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BasicCompanyRepresentation getCompany() {
		return company;
	}

	public void setCompany(BasicCompanyRepresentation company) {
		this.company = company;
	}

	public BasicEmployeeRepresentation getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(BasicEmployeeRepresentation purchaser) {
		this.purchaser = purchaser;
	}

	public List<OrderItemRepresentation> getItems() {
		return items;
	}

	public void setItems(List<OrderItemRepresentation> items) {
		this.items = items;
	}

	public float getVAT() {
		return VAT;
	}

	public void setVAT(float VAT) {
		this.VAT = VAT;
	}

	public InvoiceStatus getStatus() {
		return status;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}
}
