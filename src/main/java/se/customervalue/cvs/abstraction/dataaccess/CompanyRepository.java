package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Company;

import javax.transaction.Transactional;

@Transactional
public interface CompanyRepository extends JpaRepository<Company, Long> {
	Company findByRegistrationNumber(String registrationNumber);
}
