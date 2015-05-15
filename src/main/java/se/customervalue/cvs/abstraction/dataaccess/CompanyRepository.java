package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Company;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface CompanyRepository extends JpaRepository<Company, Long> {
	Company findByRegistrationNumber(String registrationNumber);
	List<Company> findByParentCompany(Company parentCompany);
}
