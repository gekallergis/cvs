package se.customervalue.cvs.dependency.externalservice.AccSys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import se.customervalue.cvs.abstraction.externalservice.AccSys.AccSysService;
import se.customervalue.cvs.abstraction.externalservice.AccSys.representation.AccSysResponseRepresentation;

@Service
public class AccSysServiceImpl implements AccSysService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override @Async
	public AccSysResponseRepresentation logPayment(String invoiceNumber, String amount) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("THREAD ERROR!!");
		}

		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

		map.add("action", "payment");
		map.add("invoiceNumber", invoiceNumber);
		map.add("amount", amount);
		AccSysResponseRepresentation response = restTemplate.postForObject("http://accsys.yummycode.com/accsys.php", map, AccSysResponseRepresentation.class);

		if(response.getStatus().equals("550")) {
			log.warn("[AccSys Service] AccSys said: " + response.getMessage());
		} else {
			log.debug("[AccSys Service] AccSys said: " + response.getMessage());
		}

		return response;
	}
}
