package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.OwnedProduct;

public class OwnedProductRepresentation {
	private int ownedProductId;

	private String name;

	private BasicCompanyRepresentation owner;

	private int quantity;

	public OwnedProductRepresentation() {}

	public OwnedProductRepresentation(OwnedProduct ownedProduct) {
		this.ownedProductId = ownedProduct.getOwnedProductId();
		this.name = ownedProduct.getProduct().getName();
		this.owner = new BasicCompanyRepresentation(ownedProduct.getOwner());
		this.quantity = ownedProduct.getQuantity();
	}

	public OwnedProductRepresentation(int ownedProductId, String name, int quantity) {
		this.ownedProductId = ownedProductId;
		this.name = name;
		this.quantity = quantity;
	}

	public int getOwnedProductId() {
		return ownedProductId;
	}

	public void setOwnedProductId(int ownedProductId) {
		this.ownedProductId = ownedProductId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BasicCompanyRepresentation getOwner() {
		return owner;
	}

	public void setOwner(BasicCompanyRepresentation owner) {
		this.owner = owner;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
