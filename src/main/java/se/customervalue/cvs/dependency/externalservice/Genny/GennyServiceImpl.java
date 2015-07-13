package se.customervalue.cvs.dependency.externalservice.Genny;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.customervalue.cvs.abstraction.externalservice.Genny.GennyService;
import se.customervalue.cvs.abstraction.externalservice.Genny.representation.GennyRequestRepresentation;
import se.customervalue.cvs.abstraction.externalservice.Genny.representation.GennyResponseRepresentation;
import se.customervalue.cvs.common.CVSConfig;

@Service
public class GennyServiceImpl implements GennyService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public GennyResponseRepresentation generate(GennyRequestRepresentation request) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());

			HttpEntity requestEntity = new HttpEntity<>(request, headers);
			ResponseEntity<GennyResponseRepresentation> response = restTemplate.exchange(CVSConfig.GENNY_SERVICE_ENDPOINT, HttpMethod.POST, requestEntity, GennyResponseRepresentation.class);

			return response.getBody();
		} catch (Exception ex) {
			// If an error was sent back by Genny return an empty response to show a generic message.
			return new GennyResponseRepresentation("", "");
		}
	}
}
