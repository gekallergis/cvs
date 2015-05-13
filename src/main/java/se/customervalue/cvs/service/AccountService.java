package se.customervalue.cvs.service;

import se.customervalue.cvs.api.exception.EmployeeNotFoundException;
import se.customervalue.cvs.api.exception.InvalidLoginCredentialsException;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.LoginCredentialsRepresentation;

public interface AccountService {
	EmployeeRepresentation login(LoginCredentialsRepresentation credentials) throws EmployeeNotFoundException, InvalidLoginCredentialsException;
}
