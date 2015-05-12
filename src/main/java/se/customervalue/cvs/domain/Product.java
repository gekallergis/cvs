package se.customervalue.cvs.domain;

import javax.persistence.*;

@Entity
public class Product {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int productId;

	private String name;

	@Lob
	private String info;

	private boolean isPopular;

	private float unitPrice;

	public Product() {}

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

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
}
