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

	@ExceptionHandler(UnimplementedFeatureException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unimplementedFeatureExceptionHandler(UnimplementedFeatureException ex) {
		return  new APIResponseRepresentation("100", "This feature has not been implemented yet! :)");
	}

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
	public APIResponseRepresentation employeeNotFoundExceptionHandler(EmployeeNotFoundException ex) {
		return new APIResponseRepresentation("111", "The requested employee was not found!");
	}

	@ExceptionHandler(CompanyNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation employeeNotFoundExceptionHandler(CompanyNotFoundException ex) {
		return new APIResponseRepresentation("112", "The requested company was not found!");
	}

	@ExceptionHandler(AttachToUmbrellaCompanyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation attachToUmbrellaCopmanyExceptionHandler(AttachToUmbrellaCompanyException ex) {
		return new APIResponseRepresentation("113", "Attaching employees to umbrella companies is not currently allowed!");
	}

	@ExceptionHandler(ProductNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation productNotFoundExceptionHandler(ProductNotFoundException ex) {
		return new APIResponseRepresentation("114", "Requested product was not found!");
	}

	@ExceptionHandler(CompanyRegistrationNumberAlreadyInUseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation duplicateCompanyRegistrationNumberExceptionHandler(CompanyRegistrationNumberAlreadyInUseException ex) {
		return new APIResponseRepresentation("115", "The specified registration number is already in use by another company, please choose a different one!");
	}

	@ExceptionHandler(EmployeeNotWorkingForCompanyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation employeeNotWorkingForCompanyExceptionHandler(EmployeeNotWorkingForCompanyException ex) {
		return new APIResponseRepresentation("116", "The specified employee does not work for the company! Currently, only employees of companies can manage them!");
	}

	@ExceptionHandler(UnsupportedCompanyHierarchyLevelException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unsupportedCompanyHierarchyLevelExceptionHandler(UnsupportedCompanyHierarchyLevelException ex) {
		return new APIResponseRepresentation("117", "Currently, company hierarchies can only be one level deep!");
	}

	@ExceptionHandler(UnpaidInvoiceQuotaReached.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unpaidInvoiceQuotaReachedExceptionHandler(UnpaidInvoiceQuotaReached ex) {
		return new APIResponseRepresentation("118", "The company has reached its unpaid invoice quota! Please contact customer support!");
	}

	@ExceptionHandler(NotEnoughOwnedProductsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation notEnoughOwnedProductsExceptionHandler(NotEnoughOwnedProductsException ex) {
		return new APIResponseRepresentation("119", "There are not enough products available to refund this order!");
	}

	@ExceptionHandler(OrderNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation orderNotFoundExceptionHandler(OrderNotFoundException ex) {
		return new APIResponseRepresentation("120", "The requested order was not found!");
	}

	@ExceptionHandler(InvoiceNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation invoiceNotFoundExceptionHandler(InvoiceNotFoundException ex) {
		return new APIResponseRepresentation("121", "The requested invoice was not found!");
	}

	@ExceptionHandler(FoundActiveProcessingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation foundActiveProcessingExceptionHandler(FoundActiveProcessingException ex) {
		return new APIResponseRepresentation("122", "", "", "A file has already been uploaded and being processed!");
	}

	@ExceptionHandler(SalesDataUploadException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation salesDataUploadExceptionHandler(SalesDataUploadException ex) {
		return new APIResponseRepresentation("123", "", "", "There was a problem uploading the file! Please try again later!");
	}

	@ExceptionHandler(InvalidEmployeeCompanyCombinationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation invalidEmployeeCompanyCombinationExceptionHandler(InvalidEmployeeCompanyCombinationException ex) {
		return new APIResponseRepresentation("124", "", "", "Current employee cannot upload for the selected company!");
	}

	@ExceptionHandler(SalesDataDeleteException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation SalesDataDeleteExceptionHandler(SalesDataDeleteException ex) {
		return new APIResponseRepresentation("125", "There was an error removing the sales data file! Please contact customer support!");
	}

	@ExceptionHandler(UnauthorizedReportGeneration.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unauthorizedReportGenerationHandler(UnauthorizedReportGeneration ex) {
		return new APIResponseRepresentation("126", "Ops! You are not allowed to generate the requested report!");
	}

	@ExceptionHandler(UnavailableOwnedProductsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation unavailableOwnedProductsExceptionHandler(UnavailableOwnedProductsException ex) {
		return new APIResponseRepresentation("127", "There are not enough purchased products to generate the requested report!");
	}

	@ExceptionHandler(ReportGenerationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIResponseRepresentation reportGenerationExceptionHandler(ReportGenerationException ex) {
		return new APIResponseRepresentation("128", "There was en error generating the report! Please try again later! If the problem persists, contact customer support!");
	}
}
