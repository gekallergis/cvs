package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.OrderHeader;
import se.customervalue.cvs.domain.OrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderHeaderRepresentation {
	private int orderHeaderId;

	private EmployeeRepresentation purchasedBy;

	private CompanyRepresentation purchasedFor;

	private String purchasedOn;

	private InvoiceRepresentation invoice;

	private List<OrderItemRepresentation> items;

	public OrderHeaderRepresentation() {}

	public OrderHeaderRepresentation(OrderHeader orderHeader) {
		this.orderHeaderId = orderHeader.getOrderHeaderId();
		this.purchasedBy = new EmployeeRepresentation(orderHeader.getPurchasedBy());
		this.purchasedFor = new CompanyRepresentation(orderHeader.getPurchasedFor());
		this.purchasedOn = orderHeader.getPurchasedOn().toString();
		this.invoice = new InvoiceRepresentation(orderHeader.getInvoice());

		List<OrderItemRepresentation> orderItemList = new ArrayList<OrderItemRepresentation>();
		for (OrderItem orderItem : orderHeader.getOrderItems()) {
			orderItemList.add(new OrderItemRepresentation(orderItem));
		}
		this.items = orderItemList;
	}

	public int getOrderHeaderId() {
		return orderHeaderId;
	}

	public void setOrderHeaderId(int orderHeaderId) {
		this.orderHeaderId = orderHeaderId;
	}

	public EmployeeRepresentation getPurchasedBy() {
		return purchasedBy;
	}

	public void setPurchasedBy(EmployeeRepresentation purchasedBy) {
		this.purchasedBy = purchasedBy;
	}

	public CompanyRepresentation getPurchasedFor() {
		return purchasedFor;
	}

	public void setPurchasedFor(CompanyRepresentation purchasedFor) {
		this.purchasedFor = purchasedFor;
	}

	public String getPurchasedOn() {
		return purchasedOn;
	}

	public void setPurchasedOn(String purchasedOn) {
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
