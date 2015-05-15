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
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;

@ControllerAdvice
@ResponseBody
public class APIErrorHandler {
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation validationErrorHandler(MethodArgumentNotValidException ex) {
		return  new APIResponseRepresentation("101", messageSource.getMessage("validation.error", null, LocaleContextHolder.getLocale()));
	}

	@ExceptionHandler({InvalidLoginCredentialsException.class, EmployeeNotFoundException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation invalidLoginCredentialsErrorHandler(Exception ex) {
		return new APIResponseRepresentation("102", messageSource.getMessage("account.login.credentials.invalid", null, LocaleContextHolder.getLocale()));
	}

	@ExceptionHandler(LoginTriesLimitExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation loginTriesLimitExceededErrorHandler(LoginTriesLimitExceededException ex) {
		return new APIResponseRepresentation("103", messageSource.getMessage("account.login.credentials.limit", null, LocaleContextHolder.getLocale()));
	}

	@ExceptionHandler(EmployeeAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation employeeAlreadyExistsErrorHandler(EmployeeAlreadyExistsException ex) {
		return new APIResponseRepresentation("104", "An employee with the provided email address already exists!");
	}

	@ExceptionHandler(CompanyAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation companyAlreadyExistsErrorHandler(CompanyAlreadyExistsException ex) {
		return new APIResponseRepresentation("105", "A company with the provided registration number already exists!");
	}

	@ExceptionHandler(ActivationKeyExpiredException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation activationExpiredErrorHandler(ActivationKeyExpiredException ex) {
		return new APIResponseRepresentation("106", "Your activation key has expired! Please contact customer support!");
	}
}
