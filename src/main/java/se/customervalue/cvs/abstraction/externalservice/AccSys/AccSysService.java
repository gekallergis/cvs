package se.customervalue.cvs.abstraction.externalservice.AccSys;

import org.springframework.scheduling.annotation.Async;
import se.customervalue.cvs.abstraction.externalservice.AccSys.representation.AccSysResponseRepresentation;

public interface AccSysService {
	@Async
	AccSysResponseRepresentation logPayment(String invoiceNumber, String amount);
}
