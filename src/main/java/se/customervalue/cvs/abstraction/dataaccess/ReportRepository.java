package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Company;
import se.customervalue.cvs.domain.Employee;
import se.customervalue.cvs.domain.Report;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ReportRepository extends JpaRepository<Report, Long> {
	List<Report> findByReporter(Employee employee);
	List<Report> findByCompany(Company company);
}
