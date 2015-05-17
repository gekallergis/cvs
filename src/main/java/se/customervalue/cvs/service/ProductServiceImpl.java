package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.ProductRepository;
import se.customervalue.cvs.api.representation.domain.ProductRepresentation;
import se.customervalue.cvs.domain.Product;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;

	@Override @Transactional
	public List<ProductRepresentation> getProducts() {
		List<ProductRepresentation> productList = new ArrayList<ProductRepresentation>();
		List<Product> allProducts = productRepository.findAll();
		for (Product product : allProducts) {
			productList.add(new ProductRepresentation(product));
		}
		return productList;
	}
}
