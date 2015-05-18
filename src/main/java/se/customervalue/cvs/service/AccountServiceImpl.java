package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.*;
import se.customervalue.cvs.abstraction.externalservice.MailService;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.*;
import se.customervalue.cvs.api.representation.domain.*;
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
	private MessageSource messageSource;

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
	public EmployeeRepresentation getEmployee(int employeeId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, EmployeeNotFoundException {
		if(loggedInEmployee.getEmployeeId() == employeeId) {
			log.debug("[Account Service] Retrieved employee info for same employee!");
			return loggedInEmployee;
		}

		Employee employee = employeeRepository.findByEmployeeId(employeeId);
		if(employee == null) {
			throw new EmployeeNotFoundException();
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Retrieved employee info for admin user!");
			return new EmployeeRepresentation(employee);
		}

		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		Company employeeCompany = employee.getEmployer();
		if(managedCompany != null && employeeCompany != null) {
			if(managedCompany.getCompanyId() == employeeCompany.getCompanyId()) {
				log.debug("[Account Service] Employee works for managed company!");
				return new EmployeeRepresentation(employee);
			} else {
				log.debug("[Account Service] Requesting employee is managing " + managedCompany + "! Requested employee works for " + employeeCompany + "!");
				List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
				log.debug("[Account Service] Managed company has " + subsidiaries.size() + " subsidiaries!");
				if(subsidiaries.size() > 0) {
					log.debug("[Account Service] Checking all subsidiaries!");
					for (Company subsidiary : subsidiaries) {
						log.debug("[Account Service] \tChecking subsidiary ID " + subsidiary.getCompanyId() + " against company ID " + employeeCompany.getCompanyId());
						if(subsidiary.getCompanyId() == employeeCompany.getCompanyId()) {
							log.debug("[Account Service] Found match! :D");
							return new EmployeeRepresentation(employee);
						}
					}
				} else {
					if(!(managedCompany.getCompanyId() == employeeCompany.getCompanyId())) {
						throw new UnauthorizedResourceAccess();
					}
					log.debug("[Account Service] Retrieved employee for company managed by requesting employee!");
				}
			}
		} else {
			throw new UnauthorizedResourceAccess();
		}

		throw new UnauthorizedResourceAccess();
	}

	@Override @Transactional
	public APIResponseRepresentation editEmployee(BasicEmployeeRepresentation editInfo, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, EmployeeEmailAlreadyInUseException {
		Employee possibleDuplicate = employeeRepository.findByEmail(editInfo.getEmail());
		Employee editEmployee = employeeRepository.findByEmployeeId(editInfo.getEmployeeId());
		if(possibleDuplicate != null && !possibleDuplicate.equals(editEmployee)) {
			throw new EmployeeEmailAlreadyInUseException();
		}

		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		log.debug("[Account Service] Editing employee info for employeeId = " + editInfo.getEmployeeId() + " while logged in as employeeId = " + currentEmployee.getEmployeeId() + "!");
		if(currentEmployee.getEmployeeId() == editInfo.getEmployeeId()) {
			updateEmployee(editInfo, currentEmployee);
			employeeRepository.save(currentEmployee);
			return new APIResponseRepresentation("005", messageSource.getMessage("account.edit.success", null, LocaleContextHolder.getLocale()));
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Editing employee info for admin user!");
			updateEmployee(editInfo, editEmployee);
			employeeRepository.save(editEmployee);
			return new APIResponseRepresentation("005", messageSource.getMessage("account.edit.success", null, LocaleContextHolder.getLocale()));
		}

		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		Company employeeCompany = editEmployee.getEmployer();
		if(managedCompany != null && employeeCompany != null) {
			if(managedCompany.getCompanyId() == employeeCompany.getCompanyId()) {
				updateEmployee(editInfo, editEmployee);
				employeeRepository.save(editEmployee);
				return new APIResponseRepresentation("005", messageSource.getMessage("account.edit.success", null, LocaleContextHolder.getLocale()));
			} else {
				List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
				if (subsidiaries.size() > 0) {
					for (Company subsidiary : subsidiaries) {
						if (subsidiary.getCompanyId() == employeeCompany.getCompanyId()) {
							updateEmployee(editInfo, editEmployee);
							employeeRepository.save(editEmployee);
							return new APIResponseRepresentation("005", messageSource.getMessage("account.edit.success", null, LocaleContextHolder.getLocale()));
						}
					}
				} else {
					if (!(managedCompany.getCompanyId() == employeeCompany.getCompanyId())) {
						log.debug("[Account Service] Attempt to edit employee working for another company than the one managend by requesting employee!");
						throw new UnauthorizedResourceAccess();
					}
					log.debug("[Account Service] Editing employee working for company managed by requesting employee!");
				}
			}
		} else {
			log.debug("[Account Service] Editing of other employees not allowed for non managers!");
			throw new UnauthorizedResourceAccess();
		}

		throw new UnauthorizedResourceAccess();
	}

	private Employee updateEmployee(BasicEmployeeRepresentation editInfo, Employee employee) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		log.debug("[Account Service] UPDATE EMPLOYEE");
		log.debug("[Account Service] ===============");
		log.debug("[Account Service] Changing first name from " + employee.getFirstName() + " to " + editInfo.getFirstName());
		employee.setFirstName(editInfo.getFirstName());
		log.debug("[Account Service] Changing last name from " + employee.getLastName() + " to " + editInfo.getLastName());
		employee.setLastName(editInfo.getLastName());
		log.debug("[Account Service] Changing email from " + employee.getEmail() + " to " + editInfo.getEmail());
		employee.setEmail(editInfo.getEmail());

		if (editInfo.getPassword() != null) {
			log.debug("[Account Service] Changing password to " + editInfo.getPassword());
			employee.setPassword(encoder.encode(editInfo.getPassword()));
		}

		log.debug("[Account Service] Removing employee from roles!");
		for (Role role : employee.getRoles()) {
			log.debug("[Account Service] Removing from role " + role.getLabel());
			role.getEmployees().remove(employee);
			roleRepository.save(role);
		}

		log.debug("[Account Service] Removing all roles from employee!");
		employee.getRoles().clear();

		log.debug("[Account Service] Adding new roles to employee!");
		List<RoleRepresentation> newRoles = editInfo.getRoles();
		for (RoleRepresentation role : newRoles) {
			log.debug("[Account Service] \tAdding role " + role.getRoleId() + "!");
			Role aRole = roleRepository.findByRoleId(role.getRoleId());
			log.debug("[Account Service] \t\tFound role entity for " + aRole.getLabel() + "!");
			aRole.getEmployees().add(employee);
			log.debug("[Account Service] \t\tFound " + aRole.getEmployees().size() + " employees that have that role!");
			employee.getRoles().add(aRole);
			log.debug("[Account Service] \t\tAdded role " + aRole.getLabel() + " to employee " + employee.getEmail() + "!");
			roleRepository.save(aRole);
		}

		return employee;
	}

	@Override
	public APIResponseRepresentation toggleEmployeeStatus(int employeeId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, EmployeeNotFoundException {
		Employee toggleEmployee = employeeRepository.findByEmployeeId(employeeId);
		if(toggleEmployee == null) {
			throw new EmployeeNotFoundException();
		}

		if(loggedInEmployee.getEmployeeId() == employeeId) {
			throw new UnauthorizedResourceAccess();
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Toggling employee for admin user!");
			toggleEmployee.setIsActive(!toggleEmployee.isActive());
			employeeRepository.save(toggleEmployee);
			String message = "The employee account was activated! Employee has to log out and then back in for changes to take effect!";
			if (!toggleEmployee.isActive()) {
				message = "The employee account was deactivated! Employee has to log out and then back in for changes to take effect!";
			}
			return new APIResponseRepresentation("006", message);
		}

		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		Company toggleEmployeeCompany = toggleEmployee.getEmployer();
		if(managedCompany != null && toggleEmployeeCompany != null) {
			log.debug("[Account Service] Requesting employee is managing " + managedCompany + "! Requested employee works for " + toggleEmployeeCompany + "!");
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			log.debug("[Account Service] Managed company has " + subsidiaries.size() + " subsidiaries!");
			if(subsidiaries.size() > 0) {
				log.debug("[Account Service] Checking all subsidiaries!");
				for (Company subsidiary : subsidiaries) {
					log.debug("[Account Service] \tChecking subsidiary ID " + subsidiary.getCompanyId() + " against company ID " + toggleEmployeeCompany.getCompanyId());
					if(subsidiary.getCompanyId() == toggleEmployeeCompany.getCompanyId()) {
						log.debug("[Account Service] Found match! :D");
						toggleEmployee.setIsActive(!toggleEmployee.isActive());
						employeeRepository.save(toggleEmployee);
						String message = "The employee account was activated! Employee has to log out and then back in for changes to take effect!";
						if (!toggleEmployee.isActive()) {
							message = "The employee account was deactivated! Employee has to log out and then back in for changes to take effect!";
						}
						return new APIResponseRepresentation("006", message);
					}
				}
				throw new UnauthorizedResourceAccess();
			} else {
				if(managedCompany.getCompanyId() != toggleEmployeeCompany.getCompanyId()) {
					throw new UnauthorizedResourceAccess();
				}

				log.debug("[Account Service] Toggling employee for company managed by requesting employee!");
				toggleEmployee.setIsActive(!toggleEmployee.isActive());
				employeeRepository.save(toggleEmployee);
				String message = "The employee account was activated! Employee has to log out and then back in for changes to take effect!";
				if (!toggleEmployee.isActive()) {
					message = "The employee account was deactivated! Employee has to log out and then back in for changes to take effect!";
				}
				return new APIResponseRepresentation("006", message);
			}
		} else {
			throw new UnauthorizedResourceAccess();
		}
	}

	@Override @Transactional
	public List<CompanyRepresentation> getCompanies(EmployeeRepresentation loggedInEmployee) {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		List<CompanyRepresentation> companyList = new ArrayList<CompanyRepresentation>();
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Retrieving all companies for admin user!");
			List<Company> allCompanies = companyRepository.findAll();
			for (Company company : allCompanies) {
				companyList.add(new CompanyRepresentation(company));
			}
			return companyList;
		}

		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(managedCompany != null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				log.debug("[Account Service] Retrieving all subsidiaries for group manager!");
				for (Company subsidiary : subsidiaries) {
					companyList.add(new CompanyRepresentation(subsidiary));
				}
				companyList.add(new CompanyRepresentation(managedCompany));
			} else {
				log.debug("[Account Service] Retrieving company for company manager!");
				companyList.add(new CompanyRepresentation(managedCompany));
			}
		} else {
			log.debug("[Account Service] Retrieving company for employee!");
			companyList.add(new CompanyRepresentation(currentEmployee.getEmployer()));
		}

		return companyList;
	}

	@Override @Transactional
	public List<RoleRepresentation> getRoles(EmployeeRepresentation loggedInEmployee) {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		List<RoleRepresentation> roleList = new ArrayList<RoleRepresentation>();
		List<Role> allRoles = roleRepository.findAll();
		for (Role role : allRoles) {
			roleList.add(new RoleRepresentation(role));
		}
		log.debug("[Account Service] Retrieved all system roles (" + roleList.size() + ")!");

		if(!currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Removing admin role for non admin employee!");
			roleList.remove(new RoleRepresentation(adminRole));
		}

		return roleList;
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
