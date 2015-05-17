package se.customervalue.cvs.service;

import se.customervalue.cvs.api.representation.domain.ProductRepresentation;

import java.util.List;

public interface ProductService {
	List<ProductRepresentation> getProducts();
}
