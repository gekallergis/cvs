package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Company;
import se.customervalue.cvs.domain.Product;

import javax.transaction.Transactional;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByProductId(int productId);
}
