package se.customervalue.cvs.service;

import se.customervalue.cvs.api.exception.CompanyNotFoundException;
import se.customervalue.cvs.api.exception.ProductNotFoundException;
import se.customervalue.cvs.api.exception.UnauthenticatedAccess;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.FreeProductRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.ProductRepresentation;

import java.util.List;

public interface ProductService {
	List<ProductRepresentation> getProducts();
	APIResponseRepresentation addFreeProducts(FreeProductRepresentation freeProduct, EmployeeRepresentation loggedInEmployee) throws UnauthenticatedAccess, UnauthorizedResourceAccess, ProductNotFoundException, CompanyNotFoundException;
}
