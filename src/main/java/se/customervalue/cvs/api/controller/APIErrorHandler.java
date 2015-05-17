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
	public APIResponseRepresentation validationExceptionHandler(MethodArgumentNotValidException ex) {
		return  new APIResponseRepresentation("101", messageSource.getMessage("validation.error", null, LocaleContextHolder.getLocale()));
	}

	@ExceptionHandler(InvalidLoginCredentialsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation invalidLoginCredentialsExceptionHandler(Exception ex) {
		return new APIResponseRepresentation("102", messageSource.getMessage("account.login.credentials.invalid", null, LocaleContextHolder.getLocale()));
	}

	@ExceptionHandler(LoginTriesLimitExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation loginTriesLimitExceededExceptionHandler(LoginTriesLimitExceededException ex) {
		return new APIResponseRepresentation("103", messageSource.getMessage("account.login.credentials.limit", null, LocaleContextHolder.getLocale()));
	}

	@ExceptionHandler(EmployeeAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation employeeAlreadyExistsExceptionHandler(EmployeeAlreadyExistsException ex) {
		return new APIResponseRepresentation("104", "An employee with the provided email address already exists!");
	}

	@ExceptionHandler(CompanyAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation companyAlreadyExistsExceptionHandler(CompanyAlreadyExistsException ex) {
		return new APIResponseRepresentation("105", "A company with the provided registration number already exists!");
	}

	@ExceptionHandler(ActivationKeyExpiredException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation activationExpiredExceptionHandler(ActivationKeyExpiredException ex) {
		return new APIResponseRepresentation("106", "Your activation key has expired! Please contact customer support!");
	}

	@ExceptionHandler(UnauthenticatedAccess.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unauthorizedAccessExceptionHandler(UnauthenticatedAccess ex) {
		return new APIResponseRepresentation("107", "You have to be logged in to access this resource!");
	}

	@ExceptionHandler(UnattachedEmployeeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unattachedEmployeeExceptionHandler(UnattachedEmployeeException ex) {
		return new APIResponseRepresentation("108", "Your account is not attached to a company yet! Contact customer support or your manager!");
	}

	@ExceptionHandler(UnauthorizedResourceAccess.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unauthorizedResourceAccessExceptionHandler(UnauthorizedResourceAccess ex) {
		return new APIResponseRepresentation("109", "Ops! You are not allowed to access the resources requested!");
	}

	@ExceptionHandler(EmployeeEmailAlreadyInUseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unauthorizedResourceAccessExceptionHandler(EmployeeEmailAlreadyInUseException ex) {
		return new APIResponseRepresentation("110", "The specified email address is already in use by another employee, please choose a different one!");
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation employeeNotFoundExceptionHandler(Exception ex) {
		return new APIResponseRepresentation("111", "The requested employee was not found!");
	}
}
