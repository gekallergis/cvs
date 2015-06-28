package se.customervalue.cvs.abstraction.externalservice.AccSys.representation;

public class AccSysRequestRepresentation {
	private String action;
	private String invoiceNumber;
	private float amount;

	public AccSysRequestRepresentation() {}

	public AccSysRequestRepresentation(String action, String invoiceNumber, float amount) {
		this.action = action;
		this.invoiceNumber = invoiceNumber;
		this.amount = amount;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}
