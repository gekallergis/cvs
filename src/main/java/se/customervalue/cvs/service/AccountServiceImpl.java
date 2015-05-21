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
	private ReportRepository reportRepository;

	@Autowired
	private SalesDataRepository salesDataRepository;

	@Autowired
	private OrderHeaderRepository orderHeaderRepository;

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
	public APIResponseRepresentation addEmployee(EmployeeRegistrationInfoRepresentation newEmployee, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, EmployeeAlreadyExistsException {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(!currentEmployee.getRoles().contains(adminRole) && managedCompany == null) {
			throw new UnauthorizedResourceAccess();
		}

		Employee employee;
		Employee checkEmployee = employeeRepository.findByEmail(newEmployee.getEmail());
		if(checkEmployee == null) {
			employee = new Employee(newEmployee);
		} else {
			throw new EmployeeAlreadyExistsException();
		}

		Activation newEmployeeActivation = new Activation();
		newEmployeeActivation.setEmployee(employee);
		activationRepository.save(newEmployeeActivation);
		employeeRepository.save(employee);

		mailService.send(newEmployee.getEmail(), "Welcome to CVS!", "Click <a href='" + CVSConfig.SERVICE_ENDPOINT + "activate/" + newEmployeeActivation.getActivationKey() + "'>here</a> to activate your account!");
		log.debug("[Account Service] Added new employee!");

		return new APIResponseRepresentation("010", "New employee successfully added! An email was sent to the address provided for activation!");
	}

	@Override @Transactional
	public APIResponseRepresentation deleteEmployee(int employeeId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, EmployeeNotFoundException {
		Employee deleteEmployee = employeeRepository.findByEmployeeId(employeeId);
		if(deleteEmployee == null) {
			throw new EmployeeNotFoundException();
		}

		if(loggedInEmployee.getEmployeeId() == employeeId) {
			throw new UnauthorizedResourceAccess();
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Deleting employee for admin user!");
			List<OrderHeader> deleteEmployeeOrders = orderHeaderRepository.findByPurchasedBy(deleteEmployee);
			List<Report> deleteEmployeeReports = reportRepository.findByReporter(deleteEmployee);
			List<SalesData> deleteEmployeeSalesData = salesDataRepository.findByUploader(deleteEmployee);

			if(deleteEmployeeOrders.size() > 0 || deleteEmployeeReports.size() > 0 || deleteEmployeeSalesData.size() > 0) {
				log.debug("[Account Service] Employee has orders/reports/sales data attached to it! Deactivating employee for admin user!");
				log.debug("[Account Service] Orders: " + deleteEmployeeOrders.size() + " | Reports: " + deleteEmployeeReports.size() + " | Sales Data: " + deleteEmployeeSalesData.size());
				deleteEmployee.setIsActive(false);
				employeeRepository.save(deleteEmployee);
				return new APIResponseRepresentation("009","Employee has orders/reports/sales data attached to him/her! Deactivating the account instead!");
			} else {
				log.debug("[Account Service] Removing employee from roles!");
				for (Role role : deleteEmployee.getRoles()) {
					log.debug("[Account Service] Removing from role " + role.getLabel());
					role.getEmployees().remove(deleteEmployee);
					roleRepository.save(role);
				}

				log.debug("[Account Service] Removing all roles from employee!");
				deleteEmployee.getRoles().clear();

				Company managingCompany = companyRepository.findByManagingEmployee(deleteEmployee);
				if(managingCompany != null) {
					managingCompany.setManagingEmployee(null);
					companyRepository.save(managingCompany);
				}

				if(deleteEmployee.getEmployer() != null) {
					Company deleteEmployeEmployer = deleteEmployee.getEmployer();
					deleteEmployeEmployer.getEmployees().remove(deleteEmployee);
					deleteEmployee.setEmployer(null);
					companyRepository.save(deleteEmployeEmployer);
					employeeRepository.save(deleteEmployee);
				}

				employeeRepository.delete(deleteEmployee);
				return new APIResponseRepresentation("009","Employee successfully deleted!");
			}
		}

		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		Company employeeCompany = deleteEmployee.getEmployer();
		if(managedCompany != null && employeeCompany != null) {
			if (managedCompany.getCompanyId() == employeeCompany.getCompanyId()) {
				log.debug("[Account Service] Deleting employee for company manager!");
				List<OrderHeader> deleteEmployeeOrders = orderHeaderRepository.findByPurchasedBy(deleteEmployee);
				List<Report> deleteEmployeeReports = reportRepository.findByReporter(deleteEmployee);
				List<SalesData> deleteEmployeeSalesData = salesDataRepository.findByUploader(deleteEmployee);

				if(deleteEmployeeOrders.size() > 0 || deleteEmployeeReports.size() > 0 || deleteEmployeeSalesData.size() > 0) {
					log.debug("[Account Service] Employee has orders/reports/sales data attached to it! Deactivating employee for admin user!");
					deleteEmployee.setIsActive(false);
					employeeRepository.save(deleteEmployee);
					return new APIResponseRepresentation("009","Employee has orders/reports/sales data attached to him/her! Deactivating the account instead!");
				} else {
					log.debug("[Account Service] Removing employee from roles!");
					for (Role role : deleteEmployee.getRoles()) {
						log.debug("[Account Service] Removing from role " + role.getLabel());
						role.getEmployees().remove(deleteEmployee);
						roleRepository.save(role);
					}

					log.debug("[Account Service] Removing all roles from employee!");
					deleteEmployee.getRoles().clear();

					Company managingCompany = companyRepository.findByManagingEmployee(deleteEmployee);
					if(managingCompany != null) {
						managingCompany.setManagingEmployee(null);
						companyRepository.save(managingCompany);
					}

					employeeCompany.getEmployees().remove(deleteEmployee);
					deleteEmployee.setEmployer(null);
					companyRepository.save(employeeCompany);
					employeeRepository.save(deleteEmployee);
					employeeRepository.delete(deleteEmployee);
					return new APIResponseRepresentation("009","Employee successfully deleted!");
				}
			} else {
				List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
				if (subsidiaries.size() > 0) {
					for (Company subsidiary : subsidiaries) {
						if (subsidiary.getCompanyId() == employeeCompany.getCompanyId()) {
							List<OrderHeader> deleteEmployeeOrders = orderHeaderRepository.findByPurchasedBy(deleteEmployee);
							List<Report> deleteEmployeeReports = reportRepository.findByReporter(deleteEmployee);
							List<SalesData> deleteEmployeeSalesData = salesDataRepository.findByUploader(deleteEmployee);

							if(deleteEmployeeOrders.size() > 0 || deleteEmployeeReports.size() > 0 || deleteEmployeeSalesData.size() > 0) {
								log.debug("[Account Service] Employee has orders/reports/sales data attached to it! Deactivating employee for admin user!");
								deleteEmployee.setIsActive(false);
								employeeRepository.save(deleteEmployee);
								return new APIResponseRepresentation("009","Employee has orders/reports/sales data attached to him/her! Deactivating the account instead!");
							} else {
								log.debug("[Account Service] Removing employee from roles!");
								for (Role role : deleteEmployee.getRoles()) {
									log.debug("[Account Service] Removing from role " + role.getLabel());
									role.getEmployees().remove(deleteEmployee);
									roleRepository.save(role);
								}

								log.debug("[Account Service] Removing all roles from employee!");
								deleteEmployee.getRoles().clear();

								Company managingCompany = companyRepository.findByManagingEmployee(deleteEmployee);
								if(managingCompany != null) {
									managingCompany.setManagingEmployee(null);
									companyRepository.save(managingCompany);
								}

								employeeCompany.getEmployees().remove(deleteEmployee);
								deleteEmployee.setEmployer(null);
								companyRepository.save(employeeCompany);
								employeeRepository.save(deleteEmployee);
								employeeRepository.delete(deleteEmployee);
								return new APIResponseRepresentation("009","Employee successfully deleted!");
							}
						}
					}
					throw new UnauthorizedResourceAccess();
				} else {
					log.debug("[Account Service] Attempt to delete employee working for another company than the one managend by requesting employee!");
					throw new UnauthorizedResourceAccess();
				}
			}
		} else {
			log.debug("[Account Service] Deleting of other employees not allowed for non managers!");
			throw new UnauthorizedResourceAccess();
		}
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
		if (currentEmployee.getEmployeeId() == editInfo.getEmployeeId()) {
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

	@Override @Transactional
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
	public APIResponseRepresentation addCompany(CompanyRegistrationInfoRepresentation newCompany, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, CompanyAlreadyExistsException {
		boolean isGroupManager = false;
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(managedCompany != null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				isGroupManager = true;
			}
		}

		if(!currentEmployee.getRoles().contains(adminRole) && !isGroupManager) {
			throw new UnauthorizedResourceAccess();
		}

		Company company;
		Company checkCompany = companyRepository.findByRegistrationNumber(newCompany.getRegistrationNumber());
		if(checkCompany == null) {
			company = new Company(newCompany);
			Country newCompanyCountry = countryRepository.findByCountryId(newCompany.getCountryId());
			company.setCountry(newCompanyCountry);
			if(isGroupManager) {
				company.setParentCompany(managedCompany);
				managedCompany.getSubsidiaries().add(company);
				companyRepository.save(managedCompany);
			}
			companyRepository.save(company);
		} else {
			throw new CompanyAlreadyExistsException();
		}

		log.debug("[Account Service] Added new company!");
		return new APIResponseRepresentation("011", "New company successfully added!");
	}

	@Override @Transactional
	public CompanyRepresentation getCompany(int companyId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, CompanyNotFoundException {
		Company company = companyRepository.findByCompanyId(companyId);
		if(company == null) {
			throw new CompanyNotFoundException();
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Returning company info for admin user!");
			CompanyRepresentation companyRepresentation = new CompanyRepresentation(company);
			companyRepresentation.setHierarchy(generateCompanyHierarchy(company));
			return companyRepresentation;
		}

		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(managedCompany != null) {
			if(managedCompany.getCompanyId() == company.getCompanyId()) {
				log.debug("[Account Service] Returning company info for company manager!");
				CompanyRepresentation companyRepresentation = new CompanyRepresentation(company);
				companyRepresentation.setHierarchy(generateCompanyHierarchy(company));
				return companyRepresentation;
			}

			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				for (Company subsidiary : subsidiaries) {
					if(subsidiary.getCompanyId() == company.getCompanyId()) {
						log.debug("[Account Service] Returning company info for group manager!");
						CompanyRepresentation companyRepresentation = new CompanyRepresentation(company);
						companyRepresentation.setHierarchy(generateCompanyHierarchy(company));
						return companyRepresentation;
					}
				}
			}
		} else {
			if(currentEmployee.getEmployer().getCompanyId() == company.getCompanyId()) {
				log.debug("[Account Service] Returning company info for company employee!");
				CompanyRepresentation companyRepresentation = new CompanyRepresentation(company);
				companyRepresentation.setHierarchy(generateCompanyHierarchy(company));
				return companyRepresentation;
			}
		}

		throw new UnauthorizedResourceAccess();
	}

	private CompanyHierarchyRepresentation generateCompanyHierarchy(Company company) {
		CompanyHierarchyRepresentation hierarchy = new CompanyHierarchyRepresentation();

		Company parentCompany = company.getParentCompany();
		if(parentCompany == null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(company);
			if(subsidiaries.size() > 0) {
				log.debug("[Account Service] Generating company hierarchy for group company!");
				hierarchy.setCompany(new CompanyRepresentation(company));
				List<CompanyHierarchyRepresentation> children = new ArrayList<CompanyHierarchyRepresentation>();
				for (Company subsidiary : subsidiaries) {
					CompanyHierarchyRepresentation childHierarchy = new CompanyHierarchyRepresentation();
					childHierarchy.setCompany(new CompanyRepresentation(subsidiary));
					List<EmployeeRepresentation> childEmployees = new ArrayList<EmployeeRepresentation>();
					List<Employee> childCompanyEmployees = employeeRepository.findByEmployer(subsidiary);
					for (Employee childCompanyEmployee : childCompanyEmployees) {
						childEmployees.add(new EmployeeRepresentation(childCompanyEmployee));
					}
					childHierarchy.setEmployees(childEmployees);
					children.add(childHierarchy);
				}
				hierarchy.setChildren(children);
			} else {
				log.debug("[Account Service] Generating company hierarchy for single company!");
				CompanyRepresentation missingUmbrellaCompany = new CompanyRepresentation();
				missingUmbrellaCompany.setName("No Parent Company");
				BasicEmployeeRepresentation missingManagingAccount = new BasicEmployeeRepresentation();
				missingManagingAccount.setFirstName("No");
				missingManagingAccount.setLastName("Group Manager");
				missingUmbrellaCompany.setManagingEmployee(missingManagingAccount);
				hierarchy.setCompany(missingUmbrellaCompany);
				List<CompanyHierarchyRepresentation> children = new ArrayList<CompanyHierarchyRepresentation>();
				CompanyHierarchyRepresentation childHierarchy = new CompanyHierarchyRepresentation();
				childHierarchy.setCompany(new CompanyRepresentation(company));
				List<EmployeeRepresentation> employees = new ArrayList<EmployeeRepresentation>();
				List<Employee> companyEmployees = employeeRepository.findByEmployer(company);
				for (Employee employee : companyEmployees) {
					employees.add(new EmployeeRepresentation(employee));
				}
				childHierarchy.setEmployees(employees);
				children.add(childHierarchy);
				hierarchy.setChildren(children);
			}
		} else {
			log.debug("[Account Service] Generating company hierarchy for child company!");
			hierarchy = generateCompanyHierarchy(company.getParentCompany());
		}

		return hierarchy;
	}

	@Override @Transactional
	public APIResponseRepresentation editCompany(BasicCompanyRepresentation editInfo, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, CompanyRegistrationNumberAlreadyInUseException {
		Company possibleDuplicate = companyRepository.findByRegistrationNumber(editInfo.getRegistrationNumber());
		Company editCompany = companyRepository.findByCompanyId(editInfo.getCompanyId());
		if(possibleDuplicate != null && !possibleDuplicate.equals(editCompany)) {
			throw new CompanyRegistrationNumberAlreadyInUseException();
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Editing company info for admin user!");
			updateCompany(editInfo, editCompany);
			companyRepository.save(editCompany);
			return new APIResponseRepresentation("012", "Company information successfully updated!");
		}

		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(managedCompany != null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				for (Company subsidiary : subsidiaries) {
					if(subsidiary.getCompanyId() == editCompany.getCompanyId()) {
						log.debug("[Account Service] Editing company info for group manager!");
						updateCompany(editInfo, editCompany);
						companyRepository.save(editCompany);
						return new APIResponseRepresentation("012", "Company information successfully updated!");
					}
				}
			}
		}

		throw new UnauthorizedResourceAccess();
	}

	@Override
	public APIResponseRepresentation deleteCompany(int companyId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, CompanyNotFoundException, UnimplementedFeatureException {
		throw new UnimplementedFeatureException();
	}

	private Company updateCompany(BasicCompanyRepresentation info, Company company) {
		company.setRegistrationNumber(info.getRegistrationNumber());
		company.setName(info.getName());
		company.setPrimaryAddress(info.getPrimaryAddress());
		company.setSecondaryAddress(info.getSecondaryAddress());
		company.setPostcode(info.getPostcode());
		company.setCity(info.getCity());
		company.setPhoneNumber(info.getPhoneNumber());

		Country country = countryRepository.findByCountryId(info.getCountry().getCountryId());
		Country currentCountry = countryRepository.findByCountryId(company.getCountry().getCountryId());
		currentCountry.getCompanies().remove(company);
		countryRepository.save(currentCountry);
		company.setCountry(country);

		return company;
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

	@Override @Transactional
	public APIResponseRepresentation attachEmployeeToCompany(EmployeeToCompanyAttachmentRepresentation attachment, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, EmployeeNotFoundException, CompanyNotFoundException, AttachToUmbrellaCompanyException {
		Employee employee = employeeRepository.findByEmployeeId(attachment.getEmployeeId());
		if(employee == null) {
			throw new EmployeeNotFoundException();
		}

		Company company = null;
		if(attachment.getCompanyId() > 0) {
			company = companyRepository.findByCompanyId(attachment.getCompanyId());
			if(company == null) {
				throw new CompanyNotFoundException();
			}
		}

		Company employeeCompany = employee.getEmployer();
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Attaching account to company for admin!");
			if(employeeCompany != null) {
				employeeCompany.getEmployees().remove(employee);
				companyRepository.save(employeeCompany);
			}
			if(company != null) {
				employee.setEmployer(company);
				company.getEmployees().add(employee);
				companyRepository.save(company);
			} else {
				employee.setEmployer(null);
			}
			employeeRepository.save(employee);
			return new APIResponseRepresentation("007", "Employee successfully attached to new company! Logging out and then back in is required for changes to take effect!");
		}

		if(employeeCompany == null) {
			throw new CompanyNotFoundException();
		}

		if(company != null && company.getSubsidiaries().size() > 0) {
			throw new AttachToUmbrellaCompanyException();
		}

		boolean employeeWorksForGroup = false;
		boolean companyBelongsToGroup = false;
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(managedCompany != null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				log.debug("[Account Service] Retrieving all subsidiaries for group manager!");
				for (Company subsidiary : subsidiaries) {
					if(subsidiary.getCompanyId() == employeeCompany.getCompanyId()) {
						employeeWorksForGroup = true;
					}

					if(company != null) {
						if (subsidiary.getCompanyId() == company.getCompanyId()) {
							companyBelongsToGroup = true;
						}
					} else {
						companyBelongsToGroup = true;
					}
				}

				if(employeeWorksForGroup && companyBelongsToGroup) {
					log.debug("[Account Service] Attaching account to company for group manager!");
					employeeCompany.getEmployees().remove(employee);
					if(company != null) {
						employee.setEmployer(company);
						company.getEmployees().add(employee);
						companyRepository.save(company);
					} else {
						employee.setEmployer(null);
					}
					employeeRepository.save(employee);
					companyRepository.save(employeeCompany);
					return new APIResponseRepresentation("007", "Employee successfully attached to new company! Logging out and then back in is required for changes to take effect!");
				}
			}
		}

		throw new UnauthorizedResourceAccess();
	}

	@Override @Transactional
	public APIResponseRepresentation attachManagingEmployee(ManagingEmployeeAttachmentRepresentation attachment, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, EmployeeNotFoundException, CompanyNotFoundException, EmployeeNotWorkingForCompanyException {
		Company company = companyRepository.findByCompanyId(attachment.getCompanyId());
		if(company == null) {
			throw new CompanyNotFoundException();
		}

		Employee employee = null;
		if(attachment.getEmployeeId() > 0) {
			employee = employeeRepository.findByEmployeeId(attachment.getEmployeeId());
			if(employee == null) {
				throw new EmployeeNotFoundException();
			}
		}

		if(employee != null && !company.getEmployees().contains(employee)) {
			throw new EmployeeNotWorkingForCompanyException();
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Attaching manager for admin user!");
			if(employee == null) {
				company.setManagingEmployee(null);
				companyRepository.save(company);
				return new APIResponseRepresentation("013", "Manager successfully removed from the company!");
			} else {
				company.setManagingEmployee(employee);
				companyRepository.save(company);
				return new APIResponseRepresentation("013", "Employee successfully attached as a manager to the company!");
			}
		}

		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(managedCompany != null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				log.debug("[Account Service] Attaching manager for group manager!");
				for (Company subsidiary : subsidiaries) {
					if(subsidiary.getCompanyId() == company.getCompanyId()) {
						if(employee == null) {
							company.setManagingEmployee(null);
							companyRepository.save(company);
							return new APIResponseRepresentation("013", "Manager successfully removed from the company!");
						} else {
							company.setManagingEmployee(employee);
							companyRepository.save(company);
							return new APIResponseRepresentation("013", "Employee successfully attached as a manager to the company!");
						}
					}
				}
			}
		}

		throw new UnauthorizedResourceAccess();
	}

	@Override @Transactional
	public APIResponseRepresentation attachParentCompany(ParentCompanyAttachmentRepresentation attachment, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, CompanyNotFoundException, UnsupportedCompanyHierarchyLevelException {
		Company company = companyRepository.findByCompanyId(attachment.getCompanyId());
		Company parentCompany = companyRepository.findByCompanyId(attachment.getParentCompanyId());
		if(company == null || parentCompany == null) {
			throw new CompanyNotFoundException();
		}

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Account Service] Attaching company for admin user!");
			if(parentCompany.hasParentCompany()) {
				throw new UnsupportedCompanyHierarchyLevelException();
			} else {
				company.setParentCompany(parentCompany);
				companyRepository.save(company);
				return new APIResponseRepresentation("014", "Parent company successfully attached to the company!");
			}
		}

		throw new UnauthorizedResourceAccess();
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
