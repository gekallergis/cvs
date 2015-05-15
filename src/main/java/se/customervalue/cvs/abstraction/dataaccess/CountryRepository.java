package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Country;

import javax.transaction.Transactional;

@Transactional
public interface CountryRepository extends JpaRepository<Country, Long> {
	Country findByCountryId(int countryId);
}
