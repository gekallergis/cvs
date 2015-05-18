package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.FreeProductRepresentation;
import se.customervalue.cvs.api.representation.domain.BasicEmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.ProductRepresentation;
import se.customervalue.cvs.service.ProductService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {
	@Autowired
	private HttpSession session;

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public List<ProductRepresentation> employeeEndpoint() {
		return productService.getProducts();
	}

	@RequestMapping(value = "/freeproducts", method = RequestMethod.POST)
	public APIResponseRepresentation employeeEndpoint(@RequestBody @Valid FreeProductRepresentation freeProduct) throws UnauthenticatedAccess, UnauthorizedResourceAccess, ProductNotFoundException, CompanyNotFoundException {
		EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
		if(currentlyLoggedInEmployee == null) {
			throw new UnauthenticatedAccess();
		}

		return productService.addFreeProducts(freeProduct, currentlyLoggedInEmployee);
	}

}
