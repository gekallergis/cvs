package se.customervalue.cvs.abstraction.externalservice.ExchangeRateService;

import se.customervalue.cvs.abstraction.externalservice.ExchangeRateService.exception.ExchangeRateException;
import se.customervalue.cvs.domain.Currency;

import java.math.BigDecimal;

public interface ExchangeRateService {
	void setBaseCurrency(Currency base) throws ExchangeRateException;
	BigDecimal convertToBase(Currency from, BigDecimal amount) throws ExchangeRateException;
	BigDecimal convertToBase(String fromISO4217, BigDecimal amount) throws ExchangeRateException;
	BigDecimal convert(Currency from, Currency to, BigDecimal amount) throws ExchangeRateException;
}
