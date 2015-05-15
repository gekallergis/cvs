package se.customervalue.cvs.abstraction.externalservice;

public interface MailService {
	public boolean send(String recipient, String subject, String message);
}
