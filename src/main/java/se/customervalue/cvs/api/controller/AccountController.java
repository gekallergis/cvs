package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.*;
import se.customervalue.cvs.api.representation.domain.CountryRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.service.AccountService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {
	@Autowired
	private HttpSession session;

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public EmployeeRepresentation loginEndpoint(@RequestBody @Valid LoginCredentialsRepresentation credentials) throws InvalidLoginCredentialsException, EmployeeNotFoundException, LoginTriesLimitExceededException, UnattachedEmployeeException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			EmployeeRepresentation employee = accountService.login(credentials);
			session.setAttribute("LOGGED_IN_EMPLOYEE", employee);
			return employee;
		}

		return currentlyLoggedInEmployee;
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public APIResponseRepresentation passwordResetEndpoint(@RequestBody @Valid PasswordResetCredentialsRepresentation credentials) throws EmployeeNotFoundException {
		return accountService.resetPassword(credentials);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public APIResponseRepresentation registerEndpoint(@RequestBody @Valid RegistrationInfoRepresentation registrationInfo) throws EmployeeAlreadyExistsException, CompanyAlreadyExistsException{
		return accountService.register(registrationInfo);
	}

	@RequestMapping(value = "/activate", method = RequestMethod.POST)
	public APIResponseRepresentation activateEndpoint(@RequestBody ActivationKeyRepresentation key) throws ActivationKeyExpiredException {
		return accountService.activate(key);
	}

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public List<EmployeeRepresentation> employeeEndpoint() throws UnauthorizedAccess {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthorizedAccess();
		}

		return accountService.getEmployees(currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public List<CountryRepresentation> countryListEndpoint() throws EmployeeNotFoundException {
		return accountService.getCountries();
	}
}
