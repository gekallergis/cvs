package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Activation;

import javax.transaction.Transactional;

@Transactional
public interface ActivationRepository extends JpaRepository<Activation, Long> {
	Activation findByActivationKey(String activationKey);
}
