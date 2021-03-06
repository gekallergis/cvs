package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.*;
import se.customervalue.cvs.api.representation.domain.*;
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

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public APIResponseRepresentation logoutEndpoint() {
		session.invalidate();
		return new APIResponseRepresentation("006" , "You have been logged out successfully!");
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
	public APIResponseRepresentation activateEndpoint(@RequestBody @Valid ActivationKeyRepresentation key) throws ActivationKeyExpiredException {
		return accountService.activate(key);
	}

	@RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE)
	public APIResponseRepresentation deleteEmployeeEndpoint(@PathVariable("id") int employeeId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, EmployeeNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.deleteEmployee(employeeId, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public List<EmployeeRepresentation> employeeEndpoint() throws UnauthenticatedAccess {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.getEmployees(currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/employee", method = RequestMethod.PUT)
	public APIResponseRepresentation addEmployeeEndpoint(@RequestBody @Valid EmployeeRegistrationInfoRepresentation newEmployee) throws UnauthenticatedAccess, UnauthorizedResourceAccess, EmployeeAlreadyExistsException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.addEmployee(newEmployee, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/employee", method = RequestMethod.POST)
	public APIResponseRepresentation employeeEditEndpoint(@RequestBody @Valid BasicEmployeeRepresentation editInfo) throws UnauthenticatedAccess, UnauthorizedResourceAccess, EmployeeEmailAlreadyInUseException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.editEmployee(editInfo, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
	public EmployeeRepresentation employeeEndpoint(@PathVariable("id") int employeeId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, EmployeeNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.getEmployee(employeeId, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/status/{id}", method = RequestMethod.POST)
	public APIResponseRepresentation employeeStatusEndpoint(@PathVariable("id") int employeeId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, EmployeeNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.toggleEmployeeStatus(employeeId, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/attachEmployeeToCompany", method = RequestMethod.POST)
	public APIResponseRepresentation employeeAttachmentEndpoint(@RequestBody @Valid EmployeeToCompanyAttachmentRepresentation attachment) throws UnauthenticatedAccess, UnauthorizedResourceAccess, EmployeeNotFoundException, CompanyNotFoundException, AttachToUmbrellaCompanyException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.attachEmployeeToCompany(attachment, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/attachManagingEmployee", method = RequestMethod.POST)
	public APIResponseRepresentation managingEmployeeAttachmentEndpoint(@RequestBody @Valid ManagingEmployeeAttachmentRepresentation attachment) throws UnauthenticatedAccess, UnauthorizedResourceAccess, EmployeeNotFoundException, CompanyNotFoundException, EmployeeNotWorkingForCompanyException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.attachManagingEmployee(attachment, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/attachParentCompany", method = RequestMethod.POST)
	public APIResponseRepresentation parentCompanyAttachmentEndpoint(@RequestBody @Valid ParentCompanyAttachmentRepresentation attachment) throws UnauthenticatedAccess, UnauthorizedResourceAccess, CompanyNotFoundException, UnsupportedCompanyHierarchyLevelException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.attachParentCompany(attachment, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/company", method = RequestMethod.GET)
	public List<CompanyRepresentation> companyEndpoint() throws UnauthenticatedAccess {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.getCompanies(currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
	public CompanyRepresentation companyEndpoint(@PathVariable("id") int companyId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, CompanyNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.getCompany(companyId, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/company/{id}", method = RequestMethod.DELETE)
	public APIResponseRepresentation deleteCompanyEndpoint(@PathVariable("id") int companyId) throws UnauthenticatedAccess, UnauthorizedResourceAccess, CompanyNotFoundException, UnimplementedFeatureException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.deleteCompany(companyId, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/company", method = RequestMethod.PUT)
	public APIResponseRepresentation addCompanyEndpoint(@RequestBody @Valid CompanyRegistrationInfoRepresentation newCompany) throws UnauthenticatedAccess, UnauthorizedResourceAccess, CompanyAlreadyExistsException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.addCompany(newCompany, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/company", method = RequestMethod.POST)
	public APIResponseRepresentation companyEditEndpoint(@RequestBody @Valid BasicCompanyRepresentation editInfo) throws UnauthenticatedAccess, UnauthorizedResourceAccess, CompanyRegistrationNumberAlreadyInUseException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.editCompany(editInfo, currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public List<RoleRepresentation> roleEndpoint() throws UnauthenticatedAccess {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return accountService.getRoles(currentlyLoggedInEmployee);
	}

	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public List<CountryRepresentation> countryListEndpoint() throws EmployeeNotFoundException {
		return accountService.getCountries();
	}
}
