package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import se.customervalue.cvs.api.exception.EmployeeNotFoundException;
import se.customervalue.cvs.api.exception.InvalidLoginCredentialsException;
import se.customervalue.cvs.api.representation.APIErrorRepresentation;

@ControllerAdvice
@ResponseBody
public class APIErrorHandler {
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIErrorRepresentation validationErrorHandler(MethodArgumentNotValidException ex) {
		return  new APIErrorRepresentation("101", messageSource.getMessage("validation.error", null, LocaleContextHolder.getLocale()));
	}

	@ExceptionHandler({InvalidLoginCredentialsException.class, EmployeeNotFoundException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIErrorRepresentation invalidLoginCredentialsErrorHandler(Exception ex) {
		return new APIErrorRepresentation("102", messageSource.getMessage("account.login.credentials.error", null, LocaleContextHolder.getLocale()));
	}
}
