package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int productId;

	private String name;

	@Lob
	private String info;

	private boolean isPopular;

	private float unitPrice;

	@Enumerated(EnumType.STRING)
	private ProductType type;

	@OneToMany(mappedBy="product")
	private List<OwnedProduct> purchases = new ArrayList<OwnedProduct>();

	public Product() {}

	public Product(String name, String info, boolean isPopular, ProductType type, float unitPrice) {
		this.name = name;
		this.info = info;
		this.isPopular = isPopular;
		this.type = type;
		this.unitPrice = unitPrice;
	}

	public List<OwnedProduct> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<OwnedProduct> purchases) {
		this.purchases = purchases;
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

	public ProductType getType() { return type; }

	public void setType(ProductType type) { this.type = type; }

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
}
