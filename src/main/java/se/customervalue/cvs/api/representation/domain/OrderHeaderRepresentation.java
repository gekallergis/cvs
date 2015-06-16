package se.customervalue.cvs.api.representation.domain;

import java.util.Date;
import java.util.List;

public class OrderHeaderRepresentation {
	private int orderHeaderId;

	private EmployeeRepresentation purcahsedBy;

	private CompanyRepresentation purchasedFor;

	private Date purchasedOn;

	private InvoiceRepresentation invoice;

	private List<OrderItemRepresentation> items;

	public OrderHeaderRepresentation() {}

	public int getOrderHeaderId() {
		return orderHeaderId;
	}

	public void setOrderHeaderId(int orderHeaderId) {
		this.orderHeaderId = orderHeaderId;
	}

	public EmployeeRepresentation getPurcahsedBy() {
		return purcahsedBy;
	}

	public void setPurcahsedBy(EmployeeRepresentation purcahsedBy) {
		this.purcahsedBy = purcahsedBy;
	}

	public CompanyRepresentation getPurchasedFor() {
		return purchasedFor;
	}

	public void setPurchasedFor(CompanyRepresentation purchasedFor) {
		this.purchasedFor = purchasedFor;
	}

	public Date getPurchasedOn() {
		return purchasedOn;
	}

	public void setPurchasedOn(Date purchasedOn) {
		this.purchasedOn = purchasedOn;
	}

	public InvoiceRepresentation getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceRepresentation invoice) {
		this.invoice = invoice;
	}

	public List<OrderItemRepresentation> getItems() {
		return items;
	}

	public void setItems(List<OrderItemRepresentation> items) {
		this.items = items;
	}
}
