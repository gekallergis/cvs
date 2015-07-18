package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Currency;

import javax.transaction.Transactional;

@Transactional
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
	Currency findByIso4217(String ISO4217);
}
