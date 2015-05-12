package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class OrderHeader {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderHeaderId;

	private Date purchasedOn;

	public OrderHeader() {
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
}
