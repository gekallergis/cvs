package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Company;
import se.customervalue.cvs.domain.OwnedProduct;
import se.customervalue.cvs.domain.Product;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface OwnedProductRepository extends JpaRepository<OwnedProduct, Long> {
	OwnedProduct findByOwnerAndProduct(Company company, Product product);
	List<OwnedProduct> findByOwner(Company company);
}
