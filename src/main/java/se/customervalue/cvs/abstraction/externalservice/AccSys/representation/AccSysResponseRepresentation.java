package se.customervalue.cvs.abstraction.externalservice.AccSys.representation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccSysResponseRepresentation {
	private String status;
	private String message;
	private String payment;

	public AccSysResponseRepresentation() {}

	public AccSysResponseRepresentation(String status, String message, String payment) {
		this.status = status;
		this.message = message;
		this.payment = payment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}
}
