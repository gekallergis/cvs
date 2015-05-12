package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
public class OrderHeader {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderHeaderId;

	private Date purchasedOn;

	@OneToOne
	private Invoice invoice;

	@ManyToOne
	@JoinColumn(name="purchasedBy")
	private Employee purchasedBy;

	@ManyToOne
	@JoinColumn(name="purchasedFor")
	private Company purchasedFor;

	@OneToMany(mappedBy="order")
	private Collection<OrderItem> orderItems = new ArrayList<OrderItem>();

	public OrderHeader() {}

	@PrePersist
	protected void onCreate() {
		purchasedOn = new Date();
	}

	public int getOrderHeaderId() {
		return orderHeaderId;
	}

	public void setOrderHeaderId(int orderHeaderId) {
		this.orderHeaderId = orderHeaderId;
	}

	public Date getPurchasedOn() {
		return purchasedOn;
	}

	public void setPurchasedOn(Date purchasedOn) {
		this.purchasedOn = purchasedOn;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Employee getPurchasedBy() {
		return purchasedBy;
	}

	public void setPurchasedBy(Employee purchasedBy) {
		this.purchasedBy = purchasedBy;
	}

	public Company getPurchasedFor() {
		return purchasedFor;
	}

	public void setPurchasedFor(Company purchasedFor) {
		this.purchasedFor = purchasedFor;
	}

	public Collection<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Collection<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}
