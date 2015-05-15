package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.*;
import se.customervalue.cvs.abstraction.externalservice.MailService;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.*;
import se.customervalue.cvs.api.representation.domain.CountryRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.common.CVSConfig;
import se.customervalue.cvs.domain.*;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private ActivationRepository activationRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MailService mailService;

	@Override @Transactional
	public EmployeeRepresentation login(LoginCredentialsRepresentation credentials) throws EmployeeNotFoundException, InvalidLoginCredentialsException, LoginTriesLimitExceededException, UnattachedEmployeeException {
		Employee employee = employeeRepository.findByEmail(credentials.getEmail());
		if(employee == null) {
			throw new EmployeeNotFoundException();
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		if(employee.getEmployer() == null && !employee.getRoles().contains(adminRole)) {
			throw new UnattachedEmployeeException();
		}

		if (employee.isActive()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if(encoder.matches(credentials.getPassword(), employee.getPassword())) {
				employee.setLoginTries(CVSConfig.LOGIN_MAX_TRIES);
				employeeRepository.save(employee);
				return new EmployeeRepresentation(employee);
			} else {
				employee.setLoginTries(employee.getLoginTries() - 1);
				if(employee.getLoginTries() == 0) {
					employee.setIsActive(false);
					employeeRepository.save(employee);
					throw new LoginTriesLimitExceededException();
				}
				employeeRepository.save(employee);
				throw new InvalidLoginCredentialsException();
			}
		} else {
			throw new LoginTriesLimitExceededException();
		}
	}

	@Override @Transactional
	public APIResponseRepresentation resetPassword(PasswordResetCredentialsRepresentation credentials) throws EmployeeNotFoundException {
		// Check if employee exists
		Employee employee = employeeRepository.findByEmail(credentials.getEmail());
		if(employee == null) {
			throw new EmployeeNotFoundException();
		}

		// Generate and set a new password
		String randomPassowrd = new BigInteger(130, new SecureRandom()).toString(32);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		employee.setPassword(encoder.encode(randomPassowrd));
		employeeRepository.save(employee);

		log.debug("[Account Service] Generated new password " + randomPassowrd + " for " + credentials.getEmail());

		// Email the new password
		mailService.send(employee.getEmail(), "CVS Password Reset", "Your new password is " + randomPassowrd);

		return new APIResponseRepresentation("000", "An email has been sent with your new password! It is strongly advised that you change your password imediately after logging in!");
	}

	@Override @Transactional
	public APIResponseRepresentation register(RegistrationInfoRepresentation registrationInfo) throws EmployeeAlreadyExistsException, CompanyAlreadyExistsException {
		Employee newEmployee;
		Company newCompany;

		Employee checkEmployee = employeeRepository.findByEmail(registrationInfo.getEmployeeInfo().getEmail());
		if(checkEmployee == null) {
			newEmployee = new Employee(registrationInfo.getEmployeeInfo());
		} else {
			throw new EmployeeAlreadyExistsException();
		}

		CompanyRegistrationInfoRepresentation companyInfo = registrationInfo.getCompanyInfo();
		if(!(companyInfo.getRegistrationNumber() == null || companyInfo.getName() == null || companyInfo.getPrimaryAddress() == null || companyInfo.getSecondaryAddress() == null || companyInfo.getPostcode() == null || companyInfo.getPhoneNumber() == null || companyInfo.getCountryId() == 0)) {
			Company checkCompany = companyRepository.findByRegistrationNumber(companyInfo.getRegistrationNumber());
			if(checkCompany == null) {
				newCompany = new Company(companyInfo);
				Country newCompanyCountry = countryRepository.findByCountryId(companyInfo.getCountryId());
				newCompany.setCountry(newCompanyCountry);
				newCompany.getEmployees().add(newEmployee);
				newEmployee.setEmployer(newCompany);
				companyRepository.save(newCompany);
			} else {
				throw new CompanyAlreadyExistsException();
			}
		}

		Activation newEmployeeActivation = new Activation();
		newEmployeeActivation.setEmployee(newEmployee);
		activationRepository.save(newEmployeeActivation);
		employeeRepository.save(newEmployee);

		mailService.send(newEmployee.getEmail(), "Welcome to CVS!", "Click <a href='" + CVSConfig.SERVICE_ENDPOINT + "activate/" + newEmployeeActivation.getActivationKey() + "'>here</a> to activate your account!");
		log.debug("[Account Service] Got registration request for employee " + newEmployee.getFirstName() + " " + newEmployee.getLastName());

		return new APIResponseRepresentation("002", "Welcome! Activate your account and login!");
	}

	@Override @Transactional
	public APIResponseRepresentation activate(ActivationKeyRepresentation activationKey) throws ActivationKeyExpiredException {
		log.debug("[Account Service] Got activation request with key " + activationKey.getKey());

		Activation checkActivation = activationRepository.findByActivationKey(activationKey.getKey());
		if(checkActivation == null) {
			log.debug("[Account Service] Activation key not found!");
			throw new ActivationKeyExpiredException();
		} else {
			log.debug("[Account Service] Activation key found!");
			checkActivation.getEmployee().setIsActive(true);
			activationRepository.delete(checkActivation);
		}
		return new APIResponseRepresentation("004", "Your account has been activated! You can now login!");
	}

	@Override @Transactional
	public List<EmployeeRepresentation> getEmployees(EmployeeRepresentation loggedInEmployee) {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		List<EmployeeRepresentation> employeeList = new ArrayList<EmployeeRepresentation>();
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Retrieving all employees for admin user!");
			List<Employee> allEmployees = employeeRepository.findAll();
			for (Employee employee : allEmployees) {
				employeeList.add(new EmployeeRepresentation(employee));
			}
			return employeeList;
		}

		Company currentCompany = currentEmployee.getEmployer();
		Employee currentCompanyManager = currentCompany.getManagingEmployee();
		if(currentCompany.hasManagingEmployee() && currentEmployee.getEmployeeId() == currentCompanyManager.getEmployeeId()) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(currentCompany);
			if(subsidiaries.size() > 0) {
				log.debug("[Account Service] Retrieving all group employees for group manager!");
				for (Company subsidiary : subsidiaries) {
					List<Employee> allSubsidiaryEmployees = employeeRepository.findByEmployer(subsidiary);
					for (Employee employee : allSubsidiaryEmployees) {
						employeeList.add(new EmployeeRepresentation(employee));
					}
				}
				employeeList.add(new EmployeeRepresentation(currentEmployee));
			} else {
				log.debug("[Account Service] Retrieving all company employees for company manager!");
				List<Employee> allCopmanyEmployees = employeeRepository.findByEmployer(currentCompany);
				for (Employee employee : allCopmanyEmployees) {
					employeeList.add(new EmployeeRepresentation(employee));
				}
			}
		} else {
			log.debug("[Account Service] Retrieving single employee for a company employee!");
			employeeList.add(new EmployeeRepresentation(currentEmployee));
		}

		return employeeList;
	}

	@Override @Transactional
	public List<CountryRepresentation> getCountries() {
		List<CountryRepresentation> countryListRepresentations = new ArrayList<CountryRepresentation>();

		List<Country> countryList = countryRepository.findAll();
		for (Country country : countryList) {
			countryListRepresentations.add(new CountryRepresentation(country));
		}

		return countryListRepresentations;
	}

	@Override @Transactional @Scheduled(fixedDelay=600000)
	public void cleanUpActivationKeys() {
		Instant now = (new Date()).toInstant();
		List<Activation> activationsList = activationRepository.findAll();
		for (Activation activation : activationsList) {
			Instant registrationTime = activation.getRegistrationDate().toInstant();
			long activationAge = registrationTime.until(now, ChronoUnit.MINUTES);
			if(activationAge > CVSConfig.ACTIVATION_KEY_LIFETIME_MINUTES) {
				activationRepository.delete(activation);
				log.debug("[Account Service] Cleaned up activation key " + activation.getActivationKey() + " for account " + activation.getEmployee().getEmail() + "!");
			}
		}
	}
}
