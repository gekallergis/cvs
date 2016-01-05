package se.customervalue.cvs.dependency.externalservice.Fixer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.customervalue.cvs.abstraction.externalservice.ExchangeRateService.ExchangeRateService;
import se.customervalue.cvs.abstraction.externalservice.ExchangeRateService.exception.ExchangeRateException;
import se.customervalue.cvs.dependency.externalservice.Fixer.representation.FixerResponseRepresentation;
import se.customervalue.cvs.domain.Currency;

import java.math.BigDecimal;

@Service
public class Fixer implements ExchangeRateService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final String FIXER_ENDPOINT = "http://api.fixer.io/latest?base=";

	private FixerResponseRepresentation cache = null;

	@Override
	public void setBaseCurrency(Currency base) throws ExchangeRateException {
		log.debug("[Fixer] Setting base currency to " + base.getIso4217());
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());

			HttpEntity requestEntity = new HttpEntity<>(headers);
			ResponseEntity<FixerResponseRepresentation> response = restTemplate.exchange(FIXER_ENDPOINT + base.getIso4217(), HttpMethod.GET, requestEntity, FixerResponseRepresentation.class);

			cache = response.getBody();
			cache.getRates().put(base.getIso4217(), new BigDecimal("1.00"));
		} catch (Exception ex) {
			throw new ExchangeRateException();
		}
	}

	@Override
	public BigDecimal convertToBase(Currency from, BigDecimal amount) throws ExchangeRateException {
		BigDecimal convertedAmount = amount.divide(cache.getRates().get(from.getIso4217()), 2, BigDecimal.ROUND_HALF_EVEN);
		return convertedAmount;
	}

	@Override
	public BigDecimal convertToBase(String fromISO4217, BigDecimal amount) throws ExchangeRateException {
		BigDecimal convertedAmount = amount.divide(cache.getRates().get(fromISO4217), 2, BigDecimal.ROUND_HALF_EVEN);
		return convertedAmount;
	}

	@Override
	public BigDecimal convert(Currency from, Currency to, BigDecimal amount) throws ExchangeRateException {
		throw new ExchangeRateException();
	}
}
