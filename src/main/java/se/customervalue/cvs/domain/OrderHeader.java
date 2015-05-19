package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class OrderHeader {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderHeaderId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date purchasedOn;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_OrderHeaderInvoice"))
	private Invoice invoice;

	@ManyToOne
	@JoinColumn(name="purchasedBy", foreignKey = @ForeignKey(name = "FK_OrderHeaderPurchasedBy"))
	private Employee purchasedBy;

	@ManyToOne
	@JoinColumn(name="purchasedFor", foreignKey = @ForeignKey(name = "FK_OrderHeaderPurchasedFor"))
	private Company purchasedFor;

	@OneToMany(mappedBy="order")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	public OrderHeader() {}

	public OrderHeader(Date purchasedOn) {
		this.purchasedOn = purchasedOn;
	}

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

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}
