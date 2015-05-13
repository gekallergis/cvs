package se.customervalue.cvs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.EmployeeRepository;
import se.customervalue.cvs.api.exception.EmployeeNotFoundException;
import se.customervalue.cvs.api.exception.InvalidLoginCredentialsException;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.LoginCredentialsRepresentation;
import se.customervalue.cvs.domain.Employee;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public EmployeeRepresentation login(LoginCredentialsRepresentation credentials) throws EmployeeNotFoundException, InvalidLoginCredentialsException {
		Employee employee = employeeRepository.findByEmail(credentials.getEmail());
		if(employee == null) {
			throw new EmployeeNotFoundException();
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(encoder.matches(credentials.getPassword(), employee.getPassword())) {
			EmployeeRepresentation er =  new EmployeeRepresentation();
			er.setEmail(employee.getEmail());
			er.setEmployeeId(employee.getEmployeeId());
			er.setFirstName(employee.getFirstName());
			er.setLastName(employee.getLastName());
			return er;
		}

		throw new InvalidLoginCredentialsException();
	}
}
