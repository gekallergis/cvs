package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.OrderItem;

public class OrderItemRepresentation {
	private int productId;

	private String name;

	private int quantity;

	private float unitPrice;

	private String info;

	public OrderItemRepresentation() {}

	public OrderItemRepresentation(OrderItem orderItem) {
		this.name = orderItem.getName();
		this.quantity = orderItem.getQuantity();
		this.unitPrice = orderItem.getUnitPrice();
	}

	public OrderItemRepresentation(String name, int quantity, float unitPrice) {
		this.name = name;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
