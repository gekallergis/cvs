package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.OrderItem;

import javax.transaction.Transactional;

@Transactional
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
