package se.customervalue.cvs.domain;

import javax.persistence.*;

@Entity
public class OrderItem {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderItemId;

	private String name;

	private String description;

	private int quantity;

	private float unitPrice;

	@ManyToOne
	@JoinColumn(name="orderHeaderId", foreignKey = @ForeignKey(name = "FK_OrderItemOrderHeader"))
	private OrderHeader order;

	public OrderItem() {}

	public OrderItem(String name, String description, int quantity, float unitPrice) {
		this.name = name;
		this.description = description;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	public OrderHeader getOrder() {
		return order;
	}

	public void setOrder(OrderHeader order) {
		this.order = order;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
}
