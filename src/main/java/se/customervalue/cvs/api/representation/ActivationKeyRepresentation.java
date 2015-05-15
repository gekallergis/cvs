package se.customervalue.cvs.api.representation;

public class ActivationKeyRepresentation {

	private String key = "";

	public ActivationKeyRepresentation() {}

	public ActivationKeyRepresentation(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
