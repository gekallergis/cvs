package se.customervalue.cvs.api.representation;

import org.hibernate.validator.constraints.Email;

public class PasswordResetCredentialsRepresentation {

	@Email
	private String email = "";

	public PasswordResetCredentialsRepresentation() {}

	public PasswordResetCredentialsRepresentation(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
