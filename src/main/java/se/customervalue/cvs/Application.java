package se.customervalue.cvs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import se.customervalue.cvs.abstraction.dataaccess.*;
import se.customervalue.cvs.domain.*;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SpringBootApplication
@EnableWebSecurity // Disable Spring Security
@EnableAsync
@EnableScheduling
//@EnableRedisHttpSession
public class Application extends WebMvcConfigurerAdapter implements CommandLineRunner {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OrderHeaderRepository orderHeaderRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OwnedProductRepository ownedProductRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private SalesDataRepository salesDataRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private SystemLogEntryRepository systemLogEntryRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	@Transactional
	public void run(String... strings) throws Exception {
		// Create test data!
		SystemLogEntry logEntry1 = new SystemLogEntry("A new invoice was issued for Microsoft at 4000SEK.", "Invoice Issued", SystemLogEntryType.FINANCE);
		SystemLogEntry logEntry2 = new SystemLogEntry("Microsoft successfully generated a report.", "Report Generation", SystemLogEntryType.REPORT);
		SystemLogEntry logEntry3 = new SystemLogEntry("Began generation of graphs for Microsoft NewBiz report.", "Graph Generation", SystemLogEntryType.REPORT);
		SystemLogEntry logEntry4 = new SystemLogEntry("Saab added a new company named 'Saab Norway'.", "Added New Company", SystemLogEntryType.COMPANY);
		SystemLogEntry logEntry5 = new SystemLogEntry("Updated account information for user George Kallergis (ID: 1345).", "Updated User Information", SystemLogEntryType.ACCOUNT);
		systemLogEntryRepository.save(logEntry1);
		systemLogEntryRepository.save(logEntry2);
		systemLogEntryRepository.save(logEntry3);
		systemLogEntryRepository.save(logEntry4);
		systemLogEntryRepository.save(logEntry5);

		Country country1 = new Country("Greece", "GR", "GRE", "300");
		Country country2 = new Country("Sweden", "SE", "SWE", "752");
		Country country3 = new Country("Norway", "NO", "NOR", "578");
		Country country4 = new Country("Denmark", "DK", "DNK", "208");
		Country country5 = new Country("Italy", "IT", "ITA", "380");
		countryRepository.save(country1);
		countryRepository.save(country2);
		countryRepository.save(country3);
		countryRepository.save(country4);
		countryRepository.save(country5);

		Currency currency1 = new Currency("Euro", "EUR", "978");
		Currency currency2 = new Currency("Norwegian krone", "NOK", "578");
		Currency currency3 = new Currency("Danish krone", "DKK", "208");
		Currency currency4 = new Currency("Swedish krona/kronor", "SEK", "752");
		currencyRepository.save(currency1);
		currencyRepository.save(currency2);
		currencyRepository.save(currency3);
		currencyRepository.save(currency4);

		Employee employee1 = new Employee("r.franklin@companya.se", "Rena", "Franklin", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee2 = new Employee("k.ryder@companya.se", "Kaylynn", "Ryder", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee3 = new Employee("w.hogarth@companya.se", "Walter", "Hogarth", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee4 = new Employee("w.royle@companya.se", "Wilmer", "Royle", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee5 = new Employee("a.horton@companya.se", "Adam", "Horton", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee6 = new Employee("m.everill@companya.se", "Maci", "Everill", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee7 = new Employee("c.freeman@companya.se", "Cletus", "Freeman", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee8 = new Employee("r.townsend@companya.se", "Rodney", "Townsend", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee9 = new Employee("c.underwood@companya.se", "Chuck", "Underwood", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee10 = new Employee("d.burnham@companyb.se", "Devyn", "Burnham", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee11 = new Employee("s.saylor@companyb.se", "Silas", "Saylor", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee12 = new Employee("f.mondy@companyb.se", "Fulk", "Mondy", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee13 = new Employee("j.myles@companyb.se", "Joan", "Myles", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee14 = new Employee("n.thompsett@companyc.se", "Narelle", "Thompsett", "123456", "assets/img/avatars/male-big.png", true);
		Employee employee15 = new Employee("d.waller@companyc.se", "Devyn", "Waller", "123456", "assets/img/avatars/male-big.png", true);
		employeeRepository.save(employee1);
		employeeRepository.save(employee2);
		employeeRepository.save(employee3);
		employeeRepository.save(employee4);
		employeeRepository.save(employee5);
		employeeRepository.save(employee6);
		employeeRepository.save(employee7);
		employeeRepository.save(employee8);
		employeeRepository.save(employee9);
		employeeRepository.save(employee10);
		employeeRepository.save(employee11);
		employeeRepository.save(employee12);
		employeeRepository.save(employee13);
		employeeRepository.save(employee14);
		employeeRepository.save(employee15);

		Role role0 = new Role("isAdmin");
		Role role1 = new Role("canPurchaseProducts");
		Role role2 = new Role("canViewSalesData");
		Role role3 = new Role("canUploadSalesData");
		Role role4 = new Role("canDeleteSalesData");
		Role role5 = new Role("canDownloadSalesData");
		Role role6 = new Role("canViewOwnedProducts");
		Role role7 = new Role("canGenerateReport");
		Role role8 = new Role("canDownloadReport");
		Role role9 = new Role("canDeleteReport");
		Role role10 = new Role("canViewOrders");
		Role role11 = new Role("canViewInvoices");
		Role role12 = new Role("canViewProfiles");
		Role role13 = new Role("canViewCompanies");
		Role role14 = new Role("canViewSystemLog");
		roleRepository.save(role0);
		roleRepository.save(role1);
		roleRepository.save(role2);
		roleRepository.save(role3);
		roleRepository.save(role4);
		roleRepository.save(role5);
		roleRepository.save(role6);
		roleRepository.save(role7);
		roleRepository.save(role8);
		roleRepository.save(role9);
		roleRepository.save(role10);
		roleRepository.save(role11);
		roleRepository.save(role12);
		roleRepository.save(role13);
		roleRepository.save(role14);

		employee1.getRoles().add(role1);
		employee1.getRoles().add(role2);
		employee1.getRoles().add(role3);
		employee1.getRoles().add(role4);
		employee1.getRoles().add(role5);
		employee2.getRoles().add(role1);
		employee2.getRoles().add(role2);
		employee2.getRoles().add(role3);
		employee2.getRoles().add(role4);
		employee2.getRoles().add(role5);
		employee3.getRoles().add(role1);
		employee3.getRoles().add(role2);
		employee3.getRoles().add(role3);
		employee3.getRoles().add(role4);
		employee3.getRoles().add(role5);
		employee4.getRoles().add(role1);
		employee4.getRoles().add(role2);
		employee4.getRoles().add(role3);
		employee4.getRoles().add(role4);
		employee4.getRoles().add(role5);
		employee5.getRoles().add(role1);
		employee5.getRoles().add(role2);
		employee5.getRoles().add(role3);
		employee5.getRoles().add(role4);
		employee5.getRoles().add(role5);
		employee6.getRoles().add(role1);
		employee6.getRoles().add(role2);
		employee6.getRoles().add(role3);
		employee6.getRoles().add(role4);
		employee6.getRoles().add(role5);
		employee7.getRoles().add(role1);
		employee7.getRoles().add(role2);
		employee7.getRoles().add(role3);
		employee7.getRoles().add(role4);
		employee7.getRoles().add(role5);
		employee8.getRoles().add(role1);
		employee8.getRoles().add(role2);
		employee8.getRoles().add(role3);
		employee8.getRoles().add(role4);
		employee8.getRoles().add(role5);
		employee9.getRoles().add(role1);
		employee9.getRoles().add(role2);
		employee9.getRoles().add(role3);
		employee9.getRoles().add(role4);
		employee9.getRoles().add(role5);
		employee10.getRoles().add(role1);
		employee10.getRoles().add(role2);
		employee10.getRoles().add(role3);
		employee10.getRoles().add(role4);
		employee10.getRoles().add(role5);
		employee11.getRoles().add(role1);
		employee11.getRoles().add(role2);
		employee11.getRoles().add(role3);
		employee11.getRoles().add(role4);
		employee11.getRoles().add(role5);
		employee12.getRoles().add(role1);
		employee12.getRoles().add(role2);
		employee12.getRoles().add(role3);
		employee12.getRoles().add(role4);
		employee12.getRoles().add(role5);
		employee13.getRoles().add(role1);
		employee13.getRoles().add(role2);
		employee13.getRoles().add(role3);
		employee13.getRoles().add(role4);
		employee13.getRoles().add(role5);
		employee14.getRoles().add(role1);
		employee14.getRoles().add(role2);
		employee14.getRoles().add(role3);
		employee14.getRoles().add(role4);
		employee14.getRoles().add(role5);
		employee15.getRoles().add(role0);
		employee15.getRoles().add(role1);
		employee15.getRoles().add(role2);
		employee15.getRoles().add(role3);
		employee15.getRoles().add(role4);
		employee15.getRoles().add(role5);

		role0.getEmployees().add(employee15);
		role1.getEmployees().add(employee1);
		role1.getEmployees().add(employee2);
		role1.getEmployees().add(employee3);
		role1.getEmployees().add(employee4);
		role1.getEmployees().add(employee5);
		role1.getEmployees().add(employee6);
		role1.getEmployees().add(employee7);
		role1.getEmployees().add(employee8);
		role1.getEmployees().add(employee9);
		role1.getEmployees().add(employee10);
		role1.getEmployees().add(employee11);
		role1.getEmployees().add(employee12);
		role1.getEmployees().add(employee13);
		role1.getEmployees().add(employee14);
		role1.getEmployees().add(employee15);
		role2.getEmployees().add(employee1);
		role2.getEmployees().add(employee2);
		role2.getEmployees().add(employee3);
		role2.getEmployees().add(employee4);
		role2.getEmployees().add(employee5);
		role2.getEmployees().add(employee6);
		role2.getEmployees().add(employee7);
		role2.getEmployees().add(employee8);
		role2.getEmployees().add(employee9);
		role2.getEmployees().add(employee10);
		role2.getEmployees().add(employee11);
		role2.getEmployees().add(employee12);
		role2.getEmployees().add(employee13);
		role2.getEmployees().add(employee14);
		role2.getEmployees().add(employee15);
		role3.getEmployees().add(employee1);
		role3.getEmployees().add(employee2);
		role3.getEmployees().add(employee3);
		role3.getEmployees().add(employee4);
		role3.getEmployees().add(employee5);
		role3.getEmployees().add(employee6);
		role3.getEmployees().add(employee7);
		role3.getEmployees().add(employee8);
		role3.getEmployees().add(employee9);
		role3.getEmployees().add(employee10);
		role3.getEmployees().add(employee11);
		role3.getEmployees().add(employee12);
		role3.getEmployees().add(employee13);
		role3.getEmployees().add(employee14);
		role3.getEmployees().add(employee15);
		role4.getEmployees().add(employee1);
		role4.getEmployees().add(employee2);
		role4.getEmployees().add(employee3);
		role4.getEmployees().add(employee4);
		role4.getEmployees().add(employee5);
		role4.getEmployees().add(employee6);
		role4.getEmployees().add(employee7);
		role4.getEmployees().add(employee8);
		role4.getEmployees().add(employee9);
		role4.getEmployees().add(employee10);
		role4.getEmployees().add(employee11);
		role4.getEmployees().add(employee12);
		role4.getEmployees().add(employee13);
		role4.getEmployees().add(employee14);
		role4.getEmployees().add(employee15);
		role5.getEmployees().add(employee1);
		role5.getEmployees().add(employee2);
		role5.getEmployees().add(employee3);
		role5.getEmployees().add(employee4);
		role5.getEmployees().add(employee5);
		role5.getEmployees().add(employee6);
		role5.getEmployees().add(employee7);
		role5.getEmployees().add(employee8);
		role5.getEmployees().add(employee9);
		role5.getEmployees().add(employee10);
		role5.getEmployees().add(employee11);
		role5.getEmployees().add(employee12);
		role5.getEmployees().add(employee13);
		role5.getEmployees().add(employee14);
		role5.getEmployees().add(employee15);

		Company companya = new Company("Company A", "+1 800-955-9007", "AX95-484W", "Stockholm", "Hanstavagen 12", "Sveavegen 3", "19845");
		Company companya1 = new Company("Company A1", "+1 800-955-9008", "AX95-485W", "Athens", "Hanstavagen 12", "Sveavegen 3", "19845");
		Company companya2 = new Company("Company A2", "+1 800-955-9009", "AX95-486W", "Oslo", "Hanstavagen 12", "Sveavegen 3", "19845");
		Company companyb = new Company("Company B", "+1 800-955-9010", "AG32-621F", "Copenhagen", "Hanstavagen 12", "Sveavegen 3", "19845");
		companyRepository.save(companya);
		companyRepository.save(companya1);
		companyRepository.save(companya2);
		companyRepository.save(companyb);

		companya.getSubsidiaries().add(companya1);
		companya1.setParentCompany(companya);
		companya.getSubsidiaries().add(companya2);
		companya2.setParentCompany(companya);

		companya.setCountry(country2);
		country2.getCompanies().add(companya);
		companya.getEmployees().add(employee1);
		employee1.setEmployer(companya);
		companya.setManagingEmployee(employee1);

		companya1.setCountry(country1);
		country1.getCompanies().add(companya1);
		companya1.getEmployees().add(employee2);
		employee2.setEmployer(companya1);
		companya1.getEmployees().add(employee3);
		employee3.setEmployer(companya1);
		companya1.getEmployees().add(employee4);
		employee4.setEmployer(companya1);
		companya1.getEmployees().add(employee5);
		employee5.setEmployer(companya1);
		companya1.setManagingEmployee(employee2);

		companya2.setCountry(country3);
		country3.getCompanies().add(companya2);
		companya2.getEmployees().add(employee6);
		employee6.setEmployer(companya2);
		companya2.getEmployees().add(employee7);
		employee7.setEmployer(companya2);
		companya2.getEmployees().add(employee8);
		employee8.setEmployer(companya2);
		companya2.getEmployees().add(employee9);
		employee9.setEmployer(companya2);
		companya2.setManagingEmployee(employee6);

		companyb.setCountry(country4);
		country4.getCompanies().add(companyb);
		companyb.getEmployees().add(employee10);
		employee10.setEmployer(companyb);
		companyb.getEmployees().add(employee11);
		employee11.setEmployer(companyb);
		companyb.getEmployees().add(employee12);
		employee12.setEmployer(companyb);
		companyb.getEmployees().add(employee13);
		employee13.setEmployer(companyb);
		companyb.setManagingEmployee(employee10);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +10);

		Invoice invoice1 = new Invoice("AA-454-4567-00", cal.getTime(), 18.0f, InvoiceStatus.UNPAID);
		Invoice invoice2 = new Invoice("AB-454-4136-23", cal.getTime(), 18.0f, InvoiceStatus.PAID);
		Invoice invoice3 = new Invoice("AB-454-4136-25", cal.getTime(), 18.0f, InvoiceStatus.PAID);
		Invoice invoice4 = new Invoice("AS-454-4156-45", cal.getTime(), 18.0f, InvoiceStatus.REFUND);
		Invoice invoice5 = new Invoice("AB-454-4136-26", cal.getTime(), 18.0f, InvoiceStatus.PAID);
		Invoice invoice6 = new Invoice("AS-454-4156-45", cal.getTime(), 18.0f, InvoiceStatus.REFUND);
		Invoice invoice7 = new Invoice("AB-454-4136-29", cal.getTime(), 18.0f, InvoiceStatus.UNPAID);
		Invoice invoice8 = new Invoice("AS-454-4156-45", cal.getTime(), 18.0f, InvoiceStatus.REFUND);
		Invoice invoice9 = new Invoice("AB-454-4136-21", cal.getTime(), 18.0f, InvoiceStatus.UNPAID);
		Invoice invoice10 = new Invoice("AS-454-4156-41", cal.getTime(), 18.0f, InvoiceStatus.REFUND);
		Invoice invoice11 = new Invoice("AB-454-4136-99", cal.getTime(), 18.0f, InvoiceStatus.UNPAID);
		Invoice invoice12 = new Invoice("AS-454-4156-32", cal.getTime(), 18.0f, InvoiceStatus.PAID);
		invoiceRepository.save(invoice1);
		invoiceRepository.save(invoice2);
		invoiceRepository.save(invoice3);
		invoiceRepository.save(invoice4);
		invoiceRepository.save(invoice5);
		invoiceRepository.save(invoice6);
		invoiceRepository.save(invoice7);
		invoiceRepository.save(invoice8);
		invoiceRepository.save(invoice9);
		invoiceRepository.save(invoice10);
		invoiceRepository.save(invoice11);
		invoiceRepository.save(invoice12);

		OrderHeader order1 = new OrderHeader(new Date());
		OrderHeader order2 = new OrderHeader(new Date());
		OrderHeader order3 = new OrderHeader(new Date());
		OrderHeader order4 = new OrderHeader(new Date());
		OrderHeader order5 = new OrderHeader(new Date());
		OrderHeader order6 = new OrderHeader(new Date());
		OrderHeader order7 = new OrderHeader(new Date());
		OrderHeader order8 = new OrderHeader(new Date());
		OrderHeader order9 = new OrderHeader(new Date());
		OrderHeader order10 = new OrderHeader(new Date());
		OrderHeader order11 = new OrderHeader(new Date());
		OrderHeader order12 = new OrderHeader(new Date());
		orderHeaderRepository.save(order1);
		orderHeaderRepository.save(order2);
		orderHeaderRepository.save(order3);
		orderHeaderRepository.save(order4);
		orderHeaderRepository.save(order5);
		orderHeaderRepository.save(order6);
		orderHeaderRepository.save(order7);
		orderHeaderRepository.save(order8);
		orderHeaderRepository.save(order9);
		orderHeaderRepository.save(order10);
		orderHeaderRepository.save(order11);
		orderHeaderRepository.save(order12);

		order1.setPurchasedBy(employee9);
		order1.setPurchasedFor(employee9.getEmployer());
		employee9.getEmployer().getOrders().add(order1);
		employee9.getOrders().add(order1);
		order1.setInvoice(invoice1);
		invoice1.setOrder(order1);

		order2.setPurchasedBy(employee9);
		order2.setPurchasedFor(employee9.getEmployer());
		employee9.getEmployer().getOrders().add(order2);
		employee9.getOrders().add(order2);
		order2.setInvoice(invoice2);
		invoice2.setOrder(order2);

		order3.setPurchasedBy(employee9);
		order3.setPurchasedFor(employee9.getEmployer());
		employee9.getEmployer().getOrders().add(order3);
		employee9.getOrders().add(order3);
		order3.setInvoice(invoice3);
		invoice3.setOrder(order3);

		order4.setPurchasedBy(employee1);
		order4.setPurchasedFor(employee1.getEmployer());
		employee1.getEmployer().getOrders().add(order4);
		employee1.getOrders().add(order4);
		order4.setInvoice(invoice4);
		invoice4.setOrder(order4);

		order5.setPurchasedBy(employee7);
		order5.setPurchasedFor(employee7.getEmployer());
		employee7.getEmployer().getOrders().add(order5);
		employee7.getOrders().add(order5);
		order5.setInvoice(invoice5);
		invoice5.setOrder(order5);

		order6.setPurchasedBy(employee2);
		order6.setPurchasedFor(employee2.getEmployer());
		employee2.getEmployer().getOrders().add(order6);
		employee2.getOrders().add(order6);
		order6.setInvoice(invoice6);
		invoice6.setOrder(order6);

		order7.setPurchasedBy(employee8);
		order7.setPurchasedFor(employee8.getEmployer());
		employee8.getEmployer().getOrders().add(order7);
		employee8.getOrders().add(order7);
		order7.setInvoice(invoice7);
		invoice7.setOrder(order7);

		order8.setPurchasedBy(employee4);
		order8.setPurchasedFor(employee4.getEmployer());
		employee4.getEmployer().getOrders().add(order8);
		employee4.getOrders().add(order8);
		order8.setInvoice(invoice8);
		invoice8.setOrder(order8);

		order9.setPurchasedBy(employee5);
		order9.setPurchasedFor(employee5.getEmployer());
		employee5.getEmployer().getOrders().add(order9);
		employee5.getOrders().add(order9);
		order9.setInvoice(invoice9);
		invoice9.setOrder(order9);

		order10.setPurchasedBy(employee10);
		order10.setPurchasedFor(employee10.getEmployer());
		employee10.getEmployer().getOrders().add(order10);
		employee10.getOrders().add(order10);
		order10.setInvoice(invoice10);
		invoice10.setOrder(order10);

		order11.setPurchasedBy(employee11);
		order11.setPurchasedFor(employee11.getEmployer());
		employee11.getEmployer().getOrders().add(order11);
		employee11.getOrders().add(order11);
		order11.setInvoice(invoice11);
		invoice11.setOrder(order11);

		order12.setPurchasedBy(employee13);
		order12.setPurchasedFor(employee13.getEmployer());
		employee13.getEmployer().getOrders().add(order12);
		employee13.getOrders().add(order12);
		order12.setInvoice(invoice12);
		invoice12.setOrder(order12);

		OrderItem orderItem1 = new OrderItem("NewBiz Report", "NewBiz report generation.", 5, 100.0f);
		OrderItem orderItem2 = new OrderItem("Predictive Report", "Predictive report generation.", 1, 2000.0f);
		OrderItem orderItem3 = new OrderItem("NewBiz Report", "NewBiz report generation.", 1, 50.0f);
		OrderItem orderItem4 = new OrderItem("Predictive Report", "Predictive report generation.", 10, 1000.0f);
		OrderItem orderItem5 = new OrderItem("NewBiz Report", "NewBiz report generation.", 1, 100.0f);
		OrderItem orderItem6 = new OrderItem("Predictive Report", "Predictive report generation.", 1, 2500.0f);
		OrderItem orderItem7 = new OrderItem("NewBiz Report", "NewBiz report generation.", 5, 100.0f);
		OrderItem orderItem8 = new OrderItem("Predictive Report", "Predictive report generation.", 1, 2000.0f);
		OrderItem orderItem9 = new OrderItem("NewBiz Report", "NewBiz report generation.", 5, 100.0f);
		OrderItem orderItem10 = new OrderItem("NewBiz Report", "NewBiz report generation.", 3, 50.0f);
		OrderItem orderItem11 = new OrderItem("NewBiz Report", "NewBiz report generation.", 2, 100.0f);
		OrderItem orderItem12 = new OrderItem("Predictive Report", "Predictive report generation.", 7, 2000.0f);
		OrderItem orderItem13 = new OrderItem("Predictive Report", "Predictive report generation.", 6, 2000.0f);
		OrderItem orderItem14 = new OrderItem("NewBiz Report", "NewBiz report generation.", 12, 100.0f);
		OrderItem orderItem15 = new OrderItem("NewBiz Report", "NewBiz report generation.", 8, 100.0f);
		OrderItem orderItem16 = new OrderItem("Predictive Report", "Predictive report generation.", 1, 3000.0f);
		OrderItem orderItem17 = new OrderItem("NewBiz Report", "NewBiz report generation.", 5, 100.0f);
		orderItemRepository.save(orderItem1);
		orderItemRepository.save(orderItem2);
		orderItemRepository.save(orderItem3);
		orderItemRepository.save(orderItem4);
		orderItemRepository.save(orderItem5);
		orderItemRepository.save(orderItem6);
		orderItemRepository.save(orderItem7);
		orderItemRepository.save(orderItem8);
		orderItemRepository.save(orderItem9);
		orderItemRepository.save(orderItem10);
		orderItemRepository.save(orderItem11);
		orderItemRepository.save(orderItem12);
		orderItemRepository.save(orderItem13);
		orderItemRepository.save(orderItem14);
		orderItemRepository.save(orderItem15);
		orderItemRepository.save(orderItem16);
		orderItemRepository.save(orderItem17);

		orderItem1.setOrder(order1);
		order1.getOrderItems().add(orderItem1);
		orderItem2.setOrder(order1);
		order1.getOrderItems().add(orderItem2);
		orderItem3.setOrder(order2);
		order2.getOrderItems().add(orderItem3);
		orderItem4.setOrder(order3);
		order3.getOrderItems().add(orderItem4);
		orderItem5.setOrder(order4);
		order4.getOrderItems().add(orderItem5);
		orderItem6.setOrder(order4);
		order4.getOrderItems().add(orderItem6);
		orderItem7.setOrder(order5);
		order5.getOrderItems().add(orderItem7);
		orderItem8.setOrder(order5);
		order5.getOrderItems().add(orderItem8);
		orderItem9.setOrder(order6);
		order6.getOrderItems().add(orderItem9);
		orderItem10.setOrder(order7);
		order7.getOrderItems().add(orderItem10);
		orderItem11.setOrder(order8);
		order8.getOrderItems().add(orderItem11);
		orderItem12.setOrder(order8);
		order8.getOrderItems().add(orderItem12);
		orderItem13.setOrder(order9);
		order9.getOrderItems().add(orderItem13);
		orderItem14.setOrder(order10);
		order10.getOrderItems().add(orderItem14);
		orderItem15.setOrder(order11);
		order11.getOrderItems().add(orderItem15);
		orderItem16.setOrder(order11);
		order11.getOrderItems().add(orderItem16);
		orderItem17.setOrder(order12);
		order12.getOrderItems().add(orderItem17);

		Product product1 = new Product("NewBiz Report", "Analyze your sales data!Generate your reports online!Blah blah blah!Lalalalala!Blah blah blah!Lalalalala!Blah blah blah!", false, ProductType.NEWBIZ, 5000.0f);
		Product product2 = new Product("Predictive Report", "Predict the future!Generate your reports online!Blah blah blah!Lalalalala!Blah blah blah!Lalalalala!Blah blah blah!", true, ProductType.PREDICTIVE, 50000.0f);
		productRepository.save(product1);
		productRepository.save(product2);

		OwnedProduct ownedProduct1 = new OwnedProduct();
		ownedProduct1.setQuantity(6);
		ownedProduct1.setOwner(companya);
		companya.getOwnedProducts().add(ownedProduct1);
		ownedProduct1.setProduct(product1);
		product1.getPurchases().add(ownedProduct1);

		OwnedProduct ownedProduct2 = new OwnedProduct();
		ownedProduct2.setQuantity(11);
		ownedProduct2.setOwner(companya);
		companya.getOwnedProducts().add(ownedProduct2);
		ownedProduct2.setProduct(product2);
		product2.getPurchases().add(ownedProduct2);

		OwnedProduct ownedProduct3 = new OwnedProduct();
		ownedProduct3.setQuantity(11);
		ownedProduct3.setOwner(companya1);
		companya1.getOwnedProducts().add(ownedProduct3);
		ownedProduct3.setProduct(product1);
		product1.getPurchases().add(ownedProduct3);

		OwnedProduct ownedProduct4 = new OwnedProduct();
		ownedProduct4.setQuantity(2);
		ownedProduct4.setOwner(companya1);
		companya1.getOwnedProducts().add(ownedProduct4);
		ownedProduct4.setProduct(product2);
		product2.getPurchases().add(ownedProduct4);

		OwnedProduct ownedProduct5 = new OwnedProduct();
		ownedProduct5.setQuantity(5);
		ownedProduct5.setOwner(companya2);
		companya2.getOwnedProducts().add(ownedProduct5);
		ownedProduct5.setProduct(product1);
		product1.getPurchases().add(ownedProduct5);

		OwnedProduct ownedProduct6 = new OwnedProduct();
		ownedProduct6.setQuantity(13);
		ownedProduct6.setOwner(companya2);
		companya2.getOwnedProducts().add(ownedProduct6);
		ownedProduct6.setProduct(product2);
		product2.getPurchases().add(ownedProduct6);

		OwnedProduct ownedProduct7 = new OwnedProduct();
		ownedProduct7.setQuantity(25);
		ownedProduct7.setOwner(companyb);
		companyb.getOwnedProducts().add(ownedProduct7);
		ownedProduct7.setProduct(product1);
		product1.getPurchases().add(ownedProduct7);

		OwnedProduct ownedProduct8 = new OwnedProduct();
		ownedProduct8.setQuantity(1);
		ownedProduct8.setOwner(companyb);
		companyb.getOwnedProducts().add(ownedProduct8);
		ownedProduct8.setProduct(product2);
		product2.getPurchases().add(ownedProduct8);

		ownedProductRepository.save(ownedProduct1);
		ownedProductRepository.save(ownedProduct2);
		ownedProductRepository.save(ownedProduct3);
		ownedProductRepository.save(ownedProduct4);
		ownedProductRepository.save(ownedProduct5);
		ownedProductRepository.save(ownedProduct6);
		ownedProductRepository.save(ownedProduct7);
		ownedProductRepository.save(ownedProduct8);

		SalesData salesData1 = new SalesData("March 2014", SalesDataStatus.CHECKED, new Date(), "");
		SalesData salesData2 = new SalesData("February 2014", SalesDataStatus.ERROR, new Date(), "");
		SalesData salesData3 = new SalesData("July 2014", SalesDataStatus.PROCESSING, new Date(), "");
		SalesData salesData4 = new SalesData("January 2015", SalesDataStatus.REPLACED, new Date(), "");
		salesDataRepository.save(salesData1);
		salesDataRepository.save(salesData2);
		salesDataRepository.save(salesData3);
		salesDataRepository.save(salesData4);

		salesData1.setUploader(employee9);
		employee9.getSalesDataUploads().add(salesData1);
		salesData1.setCompany(companya1);
		companya1.getSalesData().add(salesData1);

		salesData2.setUploader(employee7);
		employee7.getSalesDataUploads().add(salesData2);
		salesData2.setCompany(companya1);
		companya1.getSalesData().add(salesData2);

		salesData3.setUploader(employee8);
		employee8.getSalesDataUploads().add(salesData3);
		salesData3.setCompany(companya2);
		companya2.getSalesData().add(salesData3);

		salesData4.setUploader(employee10);
		employee10.getSalesDataUploads().add(salesData4);
		salesData4.setCompany(companyb);
		companyb.getSalesData().add(salesData4);

		Transaction transaction1 = new Transaction("5612", new Date(), 241.92f);
		Transaction transaction2 = new Transaction("1751", new Date(), 95.36f);
		Transaction transaction3 = new Transaction("6382", new Date(), 241.92f);
		Transaction transaction4 = new Transaction("9514", new Date(), 313.6f);
		Transaction transaction5 = new Transaction("5615", new Date(), 241.92f);
		Transaction transaction6 = new Transaction("3217", new Date(), 241.92f);
		Transaction transaction7 = new Transaction("3741", new Date(), 185.92f);
		Transaction transaction8 = new Transaction("9637", new Date(), 241.92f);
		Transaction transaction9 = new Transaction("3171", new Date(), -39.2f);
		Transaction transaction10 = new Transaction("3467", new Date(), 185.92f);
		Transaction transaction11 = new Transaction("1751", new Date(), 241.92f);
		Transaction transaction12 = new Transaction("3741", new Date(), 241.92f);
		Transaction transaction13 = new Transaction("6382", new Date(), 241.92f);
		Transaction transaction14 = new Transaction("5612", new Date(), 241.92f);
		Transaction transaction15 = new Transaction("5612", new Date(), 241.92f);
		Transaction transaction16 = new Transaction("1751", new Date(), 241.92f);
		Transaction transaction17 = new Transaction("6382", new Date(), 241.92f);
		Transaction transaction18 = new Transaction("3741", new Date(), 241.92f);
		Transaction transaction19 = new Transaction("6382", new Date(), 241.92f);
		Transaction transaction20 = new Transaction("3741", new Date(), 241.92f);
		transactionRepository.save(transaction1);
		transactionRepository.save(transaction2);
		transactionRepository.save(transaction3);
		transactionRepository.save(transaction4);
		transactionRepository.save(transaction5);
		transactionRepository.save(transaction6);
		transactionRepository.save(transaction7);
		transactionRepository.save(transaction8);
		transactionRepository.save(transaction9);
		transactionRepository.save(transaction10);
		transactionRepository.save(transaction11);
		transactionRepository.save(transaction12);
		transactionRepository.save(transaction13);
		transactionRepository.save(transaction14);
		transactionRepository.save(transaction15);
		transactionRepository.save(transaction16);
		transactionRepository.save(transaction17);
		transactionRepository.save(transaction18);
		transactionRepository.save(transaction19);
		transactionRepository.save(transaction20);

		transaction1.setSalesDataBatch(salesData1);
		salesData1.getTransactions().add(transaction1);
		transaction1.setCountry(country1);
		country1.getTransactions().add(transaction1);
		transaction1.setCurrency(currency1);
		currency1.getTransactions().add(transaction1);

		transaction2.setSalesDataBatch(salesData1);
		salesData1.getTransactions().add(transaction2);
		transaction2.setCountry(country1);
		country1.getTransactions().add(transaction2);
		transaction2.setCurrency(currency1);
		currency1.getTransactions().add(transaction2);

		transaction3.setSalesDataBatch(salesData1);
		salesData1.getTransactions().add(transaction3);
		transaction3.setCountry(country1);
		country1.getTransactions().add(transaction3);
		transaction3.setCurrency(currency1);
		currency1.getTransactions().add(transaction3);

		transaction4.setSalesDataBatch(salesData1);
		salesData1.getTransactions().add(transaction4);
		transaction4.setCountry(country1);
		country1.getTransactions().add(transaction4);
		transaction4.setCurrency(currency1);
		currency1.getTransactions().add(transaction4);

		transaction5.setSalesDataBatch(salesData1);
		salesData1.getTransactions().add(transaction5);
		transaction5.setCountry(country1);
		country1.getTransactions().add(transaction5);
		transaction5.setCurrency(currency1);
		currency1.getTransactions().add(transaction5);

		transaction6.setSalesDataBatch(salesData2);
		salesData2.getTransactions().add(transaction6);
		transaction6.setCountry(country1);
		country1.getTransactions().add(transaction6);
		transaction6.setCurrency(currency1);
		currency1.getTransactions().add(transaction6);

		transaction7.setSalesDataBatch(salesData2);
		salesData2.getTransactions().add(transaction7);
		transaction7.setCountry(country1);
		country1.getTransactions().add(transaction7);
		transaction7.setCurrency(currency1);
		currency1.getTransactions().add(transaction7);

		transaction8.setSalesDataBatch(salesData2);
		salesData2.getTransactions().add(transaction8);
		transaction8.setCountry(country1);
		country1.getTransactions().add(transaction8);
		transaction8.setCurrency(currency1);
		currency1.getTransactions().add(transaction8);

		transaction9.setSalesDataBatch(salesData2);
		salesData2.getTransactions().add(transaction9);
		transaction9.setCountry(country1);
		country1.getTransactions().add(transaction9);
		transaction9.setCurrency(currency1);
		currency1.getTransactions().add(transaction9);

		transaction10.setSalesDataBatch(salesData2);
		salesData2.getTransactions().add(transaction10);
		transaction10.setCountry(country1);
		country1.getTransactions().add(transaction10);
		transaction10.setCurrency(currency1);
		currency1.getTransactions().add(transaction10);

		transaction11.setSalesDataBatch(salesData3);
		salesData3.getTransactions().add(transaction11);
		transaction11.setCountry(country1);
		country1.getTransactions().add(transaction11);
		transaction11.setCurrency(currency1);
		currency1.getTransactions().add(transaction11);

		transaction12.setSalesDataBatch(salesData3);
		salesData3.getTransactions().add(transaction12);
		transaction12.setCountry(country1);
		country1.getTransactions().add(transaction12);
		transaction12.setCurrency(currency1);
		currency1.getTransactions().add(transaction12);

		transaction13.setSalesDataBatch(salesData3);
		salesData3.getTransactions().add(transaction13);
		transaction13.setCountry(country1);
		country1.getTransactions().add(transaction13);
		transaction13.setCurrency(currency1);
		currency1.getTransactions().add(transaction13);

		transaction14.setSalesDataBatch(salesData3);
		salesData3.getTransactions().add(transaction14);
		transaction14.setCountry(country1);
		country1.getTransactions().add(transaction14);
		transaction14.setCurrency(currency1);
		currency1.getTransactions().add(transaction14);

		transaction15.setSalesDataBatch(salesData3);
		salesData3.getTransactions().add(transaction15);
		transaction15.setCountry(country1);
		country1.getTransactions().add(transaction15);
		transaction15.setCurrency(currency1);
		currency1.getTransactions().add(transaction15);

		transaction16.setSalesDataBatch(salesData4);
		salesData4.getTransactions().add(transaction16);
		transaction16.setCountry(country1);
		country1.getTransactions().add(transaction16);
		transaction16.setCurrency(currency1);
		currency1.getTransactions().add(transaction16);

		transaction17.setSalesDataBatch(salesData4);
		salesData4.getTransactions().add(transaction17);
		transaction17.setCountry(country1);
		country1.getTransactions().add(transaction17);
		transaction17.setCurrency(currency1);
		currency1.getTransactions().add(transaction17);

		transaction18.setSalesDataBatch(salesData4);
		salesData4.getTransactions().add(transaction18);
		transaction18.setCountry(country1);
		country1.getTransactions().add(transaction18);
		transaction18.setCurrency(currency1);
		currency1.getTransactions().add(transaction18);

		transaction19.setSalesDataBatch(salesData4);
		salesData4.getTransactions().add(transaction19);
		transaction19.setCountry(country1);
		country1.getTransactions().add(transaction19);
		transaction19.setCurrency(currency1);
		currency1.getTransactions().add(transaction19);

		transaction20.setSalesDataBatch(salesData4);
		salesData4.getTransactions().add(transaction20);
		transaction20.setCountry(country1);
		country1.getTransactions().add(transaction20);
		transaction20.setCurrency(currency1);
		currency1.getTransactions().add(transaction20);

		Report report1 = new Report(new Date(), "", ReportStatus.GENERATING);
		Report report2 = new Report(new Date(), "", ReportStatus.READY);
		Report report3 = new Report(new Date(), "", ReportStatus.ERROR);
		Report report4 = new Report(new Date(), "", ReportStatus.READY);
		reportRepository.save(report1);
		reportRepository.save(report2);
		reportRepository.save(report3);
		reportRepository.save(report4);

		report1.setReporter(employee9);
		employee9.getGeneratedReports().add(report1);
		report1.setCompany(companya1);
		companya1.getReports().add(report1);
		report1.setProduct(product1);
		report1.setSalesData(salesData1);
		salesData1.getReports().add(report1);

		report2.setReporter(employee9);
		employee9.getGeneratedReports().add(report2);
		report2.setCompany(companya1);
		companya1.getReports().add(report2);
		report2.setProduct(product2);
		report2.setSalesData(salesData2);
		salesData2.getReports().add(report2);

		report3.setReporter(employee9);
		employee9.getGeneratedReports().add(report3);
		report3.setCompany(companya2);
		companya2.getReports().add(report3);
		report3.setProduct(product2);
		report3.setSalesData(salesData3);
		salesData3.getReports().add(report3);

		report4.setReporter(employee9);
		employee9.getGeneratedReports().add(report4);
		report4.setCompany(companyb);
		companyb.getReports().add(report4);
		report4.setProduct(product1);
		report4.setSalesData(salesData4);
		salesData4.getReports().add(report4);
	}
}
