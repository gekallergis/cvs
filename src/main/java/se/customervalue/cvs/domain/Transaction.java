package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private int transactionId;

	private String consumerId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private float amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="salesDataBatch", foreignKey = @ForeignKey(name = "FK_TransactionSalesDataBatch"))
	private SalesData salesDataBatch;

	@ManyToOne
	@JoinColumn(name="country", foreignKey = @ForeignKey(name = "FK_TransactionCurrency"))
	private Country country;

	@ManyToOne
	@JoinColumn(name="currency", foreignKey = @ForeignKey(name = "FK_TransactionCountry"))
	private Currency currency;

	public Transaction() {}

	public Transaction(String consumerId, Date date, float amount) {
		this.consumerId = consumerId;
		this.date = date;
		this.amount = amount;
	}

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
