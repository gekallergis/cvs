package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.customervalue.cvs.api.exception.EmployeeNotFoundException;
import se.customervalue.cvs.api.exception.InvalidLoginCredentialsException;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.LoginCredentialsRepresentation;
import se.customervalue.cvs.service.AccountService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class AccountController {
	@Autowired
	private HttpSession session;

	@Autowired
	private AccountService accountService;

	AccountController() {}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public EmployeeRepresentation loginEndpoint(@RequestBody @Valid LoginCredentialsRepresentation credentials) throws InvalidLoginCredentialsException, EmployeeNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			EmployeeRepresentation employee = accountService.login(credentials);
			session.setAttribute("LOGGED_IN_EMPLOYEE", employee);
			return employee;
		}

		return currentlyLoggedInEmployee;
	}
}
