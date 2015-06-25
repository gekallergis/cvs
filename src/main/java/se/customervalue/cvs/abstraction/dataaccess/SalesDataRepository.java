package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Company;
import se.customervalue.cvs.domain.Employee;
import se.customervalue.cvs.domain.SalesData;
import se.customervalue.cvs.domain.SalesDataStatus;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface SalesDataRepository extends JpaRepository<SalesData, Long> {
	SalesData findBySalesDataId(int salesDataId);
	List<SalesData> findByUploader(Employee employee);
	List<SalesData> findByCompany(Company company);
	List<SalesData> findByCompanyAndSalesPeriodAndStatus(Company company, String salesPeriod, SalesDataStatus status);
}
