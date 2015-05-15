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
import java.util.Locale;

@SpringBootApplication
@EnableWebSecurity // Disable Spring Security
@EnableScheduling
//@EnableAsync
//@EnableRedisHttpSession
public class Application extends WebMvcConfigurerAdapter implements CommandLineRunner {
	@Autowired
	private EmployeeRepository employeeRepo;

	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private OrderHeaderRepository orderRepo;

	@Autowired
	private InvoiceRepository invoiceRepo;

	@Autowired
	private OrderItemRepository itemRepo;

	@Autowired
	private OwnedProductRepository ownedRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private ReportRepository reportRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private SalesDataRepository salesRepo;

	@Autowired
	private TransactionRepository transRepo;

	@Autowired
	private CurrencyRepository currencyRepo;

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
		Currency curr = new Currency();
		curr.setISO4217("EUR");
		curr.setName("Euro");

		Calendar caly = Calendar.getInstance();
		caly.add(Calendar.DATE, -100);

		Transaction trans = new Transaction();
		trans.setAmount(15.25f);
		trans.setConsumerId("wuiej");
		trans.setDate(caly.getTime());
		trans.setCurrency(curr);
		curr.getTransactions().add(trans);

		Country c1 = new Country();
		c1.setName("Greece");
		c1.setISO31661a2("GR");
		c1.setISO31661a3("GRE");
		c1.setNumericCode("015");
		trans.setCountry(c1);
		c1.getTransactions().add(trans);

		Country c2 = new Country();
		c2.setName("Sweden");
		c2.setISO31661a2("SE");
		c2.setISO31661a3("SWE");
		c2.setNumericCode("025");

		SalesData sd1 = new SalesData();
		sd1.setSalesPeriod("March 2015");
		sd1.setFilePath("path");
		sd1.setStatus(SalesDataStatus.CHECKED);
		trans.setSalesDataBatch(sd1);
		sd1.getTransactions().add(trans);

		Company co = new Company();
		co.setName("Microsoft");
		co.setCountry(c1);
		c1.getCompanies().add(co);
		co.getSalesData().add(sd1);
		sd1.setCompany(co);

		Company co2 = new Company();
		co2.setName("Scania");
		co2.setCountry(c2);
		c2.getCompanies().add(co2);

		Company co3 = new Company();
		co3.setName("Volvo");
		co3.setCountry(c2);
		c2.getCompanies().add(co3);

		co.getSubsidiaries().add(co2);
		co.getSubsidiaries().add(co3);
		co2.setParentCompany(co);
		co3.setParentCompany(co);

		Report rep = new Report();
		rep.setStatus(ReportStatus.GENERATING);
		rep.setFilePath("path");
		rep.setCompany(co);
		rep.setSalesData(sd1);
		co.getReports().add(rep);
		sd1.getReports().add(rep);

		Product prod1 = new Product();
		prod1.setIsPopular(true);
		prod1.setName("Product 1");
		prod1.setUnitPrice(150.00f);
		rep.setProduct(prod1);

		Product prod2 = new Product();
		prod2.setIsPopular(true);
		prod2.setName("Product 2");
		prod2.setUnitPrice(600.00f);

		Product prod3 = new Product();
		prod3.setIsPopular(true);
		prod3.setName("Product 3");
		prod3.setUnitPrice(1000.20f);

		OwnedProduct op1 = new OwnedProduct();
		op1.setQuantity(10);
		op1.setOwner(co);
		op1.setProduct(prod1);
		co.getOwnedProducts().add(op1);
		prod1.getPurchases().add(op1);

		OwnedProduct op2 = new OwnedProduct();
		op2.setQuantity(5);
		op2.setOwner(co);
		op2.setProduct(prod2);
		co.getOwnedProducts().add(op2);
		prod1.getPurchases().add(op2);

		OwnedProduct op3 = new OwnedProduct();
		op3.setQuantity(5);
		op3.setOwner(co2);
		op3.setProduct(prod3);
		co2.getOwnedProducts().add(op3);
		prod1.getPurchases().add(op3);

		Role role1 = new Role();
		role1.setLabel("canKillBill");

		Role role2 = new Role();
		role2.setLabel("canFly");

		OrderHeader order = new OrderHeader();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +10);

		Invoice invoice = new Invoice("AA-454-4113-00", cal.getTime(), 0.18f);

		order.setInvoice(invoice);
		invoice.setOrder(order);

		co.getOrders().add(order);
		order.setPurchasedFor(co);

		OrderItem item1 = new OrderItem();
		item1.setDescription("dwbvjioiwjbdf");
		item1.setName("Test Item");
		item1.setQuantity(5);
		item1.setUnitPrice(150.26f);

		order.getOrderItems().add(item1);
		item1.setOrder(order);

		OrderItem item2 = new OrderItem();
		item2.setDescription("lalalalalla");
		item2.setName("Test Item 2");
		item2.setQuantity(10);
		item2.setUnitPrice(250.26f);

		order.getOrderItems().add(item2);
		item2.setOrder(order);

		Employee emp = new Employee("kallergis.george@gmail.com", "George", "Kallergis", "123456", null, false, co);
		emp.setIsActive(true);
		emp.getRoles().add(role1);
		emp.getOrders().add(order);
		emp.getSalesDataUploads().add(sd1);
		sd1.setUploader(emp);
		emp.getGeneratedReports().add(rep);
		rep.setReporter(emp);

		order.setPurchasedBy(emp);

		Employee emp2 = new Employee("john@gmail.com", "John", "dwv", "1234567", null, false, co);
		emp2.getRoles().add(role1);
		emp2.getRoles().add(role2);

		role2.getEmployees().add(emp2);
		role1.getEmployees().add(emp);
		role1.getEmployees().add(emp2);

		co.getEmployees().add(emp);
		co.getEmployees().add(emp2);
		co.setManagingEmployee(emp2);

		invoiceRepo.save(invoice);
		orderRepo.save(order);
		roleRepo.save(role1);
		roleRepo.save(role2);
		employeeRepo.save(emp);
		employeeRepo.save(emp2);
		companyRepo.save(co);
		companyRepo.save(co2);
		companyRepo.save(co3);
		itemRepo.save(item1);
		itemRepo.save(item2);
		productRepo.save(prod1);
		productRepo.save(prod2);
		productRepo.save(prod3);
		ownedRepo.save(op1);
		ownedRepo.save(op2);
		ownedRepo.save(op3);
		reportRepo.save(rep);
		countryRepo.save(c1);
		countryRepo.save(c2);
		salesRepo.save(sd1);
		transRepo.save(trans);
		currencyRepo.save(curr);
	}
}
