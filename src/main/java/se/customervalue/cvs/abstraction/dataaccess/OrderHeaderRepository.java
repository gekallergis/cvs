package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Employee;
import se.customervalue.cvs.domain.OrderHeader;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
	List<OrderHeader> findByPurchasedBy(Employee employee);
}
