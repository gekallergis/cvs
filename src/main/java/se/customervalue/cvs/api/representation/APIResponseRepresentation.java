package se.customervalue.cvs.api.representation;

public class APIResponseRepresentation {
	private String code;
	private String message;
	private String extra;
	private String error;

	public APIResponseRepresentation() {}

	public APIResponseRepresentation(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public APIResponseRepresentation(String code, String message, String extra) {
		this.code = code;
		this.message = message;
		this.extra = extra;
	}

	public APIResponseRepresentation(String code, String message, String extra, String error) {
		this.code = code;
		this.message = message;
		this.error = error;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
