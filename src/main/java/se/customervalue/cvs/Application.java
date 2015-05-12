package se.customervalue.cvs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import se.customervalue.cvs.abstraction.dataaccess.EmployeeRepository;
import se.customervalue.cvs.abstraction.dataaccess.CompanyRepository;
import se.customervalue.cvs.abstraction.dataaccess.RoleRepository;
import se.customervalue.cvs.domain.Employee;
import se.customervalue.cvs.domain.Company;
import se.customervalue.cvs.domain.Role;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
@EnableWebSecurity // Disable Spring Security
//@EnableRedisHttpSession
public class Application implements CommandLineRunner {
	@Autowired
	private EmployeeRepository employeeRepo;

	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private RoleRepository roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... strings) throws Exception {
		System.out.println("=======================================================");
		Company co = new Company();
		co.setName("Microsoft");

		Role role1 = new Role();
		role1.setLabel("canKillBill");

		Role role2 = new Role();
		role2.setLabel("canFly");

		Employee emp = new Employee("kallergis.george@gmail.com", "George", "Kallergis", "123456", null, false, co);
		emp.getRoles().add(role1);

		Employee emp2 = new Employee("john@gmail.com", "John", "dwv", "1234567", null, false, co);
		emp2.getRoles().add(role1);
		emp2.getRoles().add(role2);

		role2.getEmployees().add(emp2);
		role1.getEmployees().add(emp);
		role1.getEmployees().add(emp2);

		co.getEmployees().add(emp);
		co.getEmployees().add(emp2);
		co.setManagingEmployee(emp2);

		roleRepo.save(role1);
		roleRepo.save(role2);
		employeeRepo.save(emp);
		employeeRepo.save(emp2);
		companyRepo.save(co);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		List<Company> comps = companyRepo.findAll();
		for (Company comp : comps) {
			System.out.println(comp.getName() + "(" + comp.getEmployees().size() + ")");
			System.out.println("\tManager: " + comp.getManagingEmployee().getFirstName() + " " + comp.getManagingEmployee().getLastName());
			System.out.println("\tEmploy List: ");
			Collection<Employee> emps = comp.getEmployees();
			for (Employee employee : emps) {
				System.out.print("\t\t" + employee.getFirstName());
				if(encoder.matches("123456", employee.getPassword())) {
					System.out.print(" pass is 123456! Roles: ");
				} else {
					System.out.print(" pass is NOT 123456! Roles: ");
				}
				Collection<Role> roles = employee.getRoles();
				for (Role role : roles) {
					System.out.print(role.getLabel() + ", ");
				}
				System.out.println();
			}
		}

		Collection<Role> roles = roleRepo.findAll();
		for (Role role : roles) {
			System.out.println("Role: " + role.getLabel());
			Collection<Employee> emps = role.getEmployees();
			for (Employee employee : emps) {
				System.out.println("\t" + employee.getFirstName());
			}
		}
		System.out.println("=======================================================");
		companyRepo.delete(co);
		System.out.println("=======================================================");
	}
}
