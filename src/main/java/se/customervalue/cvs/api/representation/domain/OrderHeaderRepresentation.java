package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.InvoiceStatus;
import se.customervalue.cvs.domain.OrderHeader;
import se.customervalue.cvs.domain.OrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderHeaderRepresentation {
	private int orderHeaderId;

	private int invoiceId;

	private String purcahsedBy;

	private String purchasedFor;

	private Date purchasedOn;

	private InvoiceStatus status;

	private List<OrderItemRepresentation> items;

	public OrderHeaderRepresentation() {}

	public OrderHeaderRepresentation(OrderHeader orderHeader) {
		this.orderHeaderId = orderHeader.getOrderHeaderId();
		this.invoiceId = orderHeader.getInvoice().getInvoiceId();
		this.purcahsedBy = orderHeader.getPurchasedBy().getFirstName() + " " + orderHeader.getPurchasedBy().getLastName();
		this.purchasedFor = orderHeader.getPurchasedFor().getName();
		this.purchasedOn = orderHeader.getPurchasedOn();
		this.status = orderHeader.getInvoice().getStatus();

		List<OrderItemRepresentation> orderItemList = new ArrayList<OrderItemRepresentation>();
		for (OrderItem orderItem : orderHeader.getOrderItems()) {
			orderItemList.add(new OrderItemRepresentation(orderItem));
		}
		this.items = orderItemList;
	}

	public OrderHeaderRepresentation(int orderHeaderId, int invoiceId, String purcahsedBy, String purchasedFor, Date purchasedOn, InvoiceStatus status) {
		this.orderHeaderId = orderHeaderId;
		this.invoiceId = invoiceId;
		this.purcahsedBy = purcahsedBy;
		this.purchasedFor = purchasedFor;
		this.purchasedOn = purchasedOn;
		this.status = status;
	}

	public int getOrderHeaderId() {
		return orderHeaderId;
	}

	public void setOrderHeaderId(int orderHeaderId) {
		this.orderHeaderId = orderHeaderId;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getPurcahsedBy() {
		return purcahsedBy;
	}

	public void setPurcahsedBy(String purcahsedBy) {
		this.purcahsedBy = purcahsedBy;
	}

	public String getPurchasedFor() {
		return purchasedFor;
	}

	public void setPurchasedFor(String purchasedFor) {
		this.purchasedFor = purchasedFor;
	}

	public Date getPurchasedOn() {
		return purchasedOn;
	}

	public void setPurchasedOn(Date purchasedOn) {
		this.purchasedOn = purchasedOn;
	}

	public InvoiceStatus getStatus() {
		return status;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}

	public List<OrderItemRepresentation> getItems() {
		return items;
	}

	public void setItems(List<OrderItemRepresentation> items) {
		this.items = items;
	}
}
