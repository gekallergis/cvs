package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int transactionId;

	private String consumerId;

	private Date date;

	private float amount;

	@ManyToOne
	@JoinColumn(name="salesDataBatch")
	private SalesData salesDataBatch;

	@ManyToOne
	@JoinColumn(name="country")
	private Country country;

	@ManyToOne
	@JoinColumn(name="currency")
	private Currency currency;

	public Transaction() {}

	public SalesData getSalesDataBatch() {
		return salesDataBatch;
	}

	public void setSalesDataBatch(SalesData salesDataBatch) {
		this.salesDataBatch = salesDataBatch;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}