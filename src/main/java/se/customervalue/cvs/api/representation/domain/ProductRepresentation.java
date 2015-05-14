package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.Product;

public class ProductRepresentation {
	private int productId;

	private String name;

	private float unitPrice;

	private String info;

	private boolean isPopular;

	public ProductRepresentation() {}

	public ProductRepresentation(Product product) {
		this.productId = product.getProductId();
		this.name = product.getName();
		this.unitPrice = product.getUnitPrice();
		this.info = product.getInfo();
		this.isPopular = product.isPopular();
	}

	public ProductRepresentation(int productId, String name, float unitPrice, String info, boolean isPopular) {
		this.productId = productId;
		this.name = name;
		this.unitPrice = unitPrice;
		this.info = info;
		this.isPopular = isPopular;
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

	public boolean isPopular() {
		return isPopular;
	}

	public void setIsPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}
}
