package se.customervalue.cvs.dependency.externalservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.externalservice.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
	@Autowired
	private JavaMailSender mailSender;

	public MailServiceImpl() {}

	@Override
	public boolean send(String recipient, String subject, String message) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;

		try {
			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setSubject(subject);
			helper.setTo(recipient);
			helper.setText(message, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			return false;
		}

		return true;
	}
}
