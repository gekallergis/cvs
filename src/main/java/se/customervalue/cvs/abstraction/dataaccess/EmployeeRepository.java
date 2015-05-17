package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Company;
import se.customervalue.cvs.domain.Employee;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Employee findByEmail(String email);
	List<Employee> findByEmployer(Company employer);
	Employee findByEmployeeId(int employeeId);
}
