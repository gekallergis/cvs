package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.OrderHeader;

import javax.transaction.Transactional;

@Transactional
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {}
