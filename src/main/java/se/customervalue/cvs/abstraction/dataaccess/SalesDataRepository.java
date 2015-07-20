package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

	@Modifying
	@Query(value = "LOAD DATA LOCAL INFILE ?1 INTO TABLE cvs.transaction FIELDS TERMINATED BY '\\t' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\r\\n' IGNORE 1 LINES (consumerId, @countryId, date, @dummy, @currencyId, amount) SET country = (SELECT countryId FROM country WHERE iso31661a2 = TRIM(BOTH '\"' FROM @countryId)), currency = (SELECT currencyId FROM currency WHERE iso4217 = TRIM(BOTH '\"' FROM @currencyId)), salesDataBatch = ?2 ;", nativeQuery = true)
	int importSalesDataBatch(String filePath, int salesDataBatchId);

	@Modifying
	@Query(value = "LOAD DATA LOCAL INFILE ?1 INTO TABLE cvs.transaction FIELDS TERMINATED BY '\\t' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (consumerId, country, date, @dummy, currency, amount) SET salesDataBatch = ?2 ;", nativeQuery = true)
	int importSalesDataBatchNoSelect(String filePath, int salesDataBatchId);
}
