package se.customervalue.cvs.service;

import org.springframework.scheduling.annotation.Scheduled;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.*;
import se.customervalue.cvs.api.representation.domain.CountryRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;

import java.util.List;

public interface AccountService {
	EmployeeRepresentation login(LoginCredentialsRepresentation credentials) throws EmployeeNotFoundException, InvalidLoginCredentialsException, LoginTriesLimitExceededException, UnattachedEmployeeException;
	APIResponseRepresentation resetPassword(PasswordResetCredentialsRepresentation credentials) throws EmployeeNotFoundException;
	APIResponseRepresentation register(RegistrationInfoRepresentation registrationInfo) throws EmployeeAlreadyExistsException, CompanyAlreadyExistsException;
	APIResponseRepresentation activate(ActivationKeyRepresentation activationKey) throws ActivationKeyExpiredException;
	List<EmployeeRepresentation> getEmployees(EmployeeRepresentation loggedInEmployee);
	List<CountryRepresentation> getCountries();

	@Scheduled
	void cleanUpActivationKeys();
}
