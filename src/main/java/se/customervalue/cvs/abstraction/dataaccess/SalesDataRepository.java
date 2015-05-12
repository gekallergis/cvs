package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.SalesData;

import javax.transaction.Transactional;

@Transactional
public interface SalesDataRepository extends JpaRepository<SalesData, Long> {}
