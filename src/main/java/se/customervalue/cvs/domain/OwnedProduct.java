package se.customervalue.cvs.domain;

import javax.persistence.*;

@Entity
public class OwnedProduct {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ownedProductId;

	private int quantity;

	@ManyToOne
	@JoinColumn(name="companyId")
	private Company owner;

	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;

	public OwnedProduct() {}

	public Company getOwner() {
		return owner;
	}

	public void setOwner(Company owner) {
		this.owner = owner;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getOwnedProductId() {
		return ownedProductId;
	}

	public void setOwnedProductId(int ownedProductId) {
		this.ownedProductId = ownedProductId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
