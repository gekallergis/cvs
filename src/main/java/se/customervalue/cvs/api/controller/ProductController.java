package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.customervalue.cvs.api.representation.domain.ProductRepresentation;
import se.customervalue.cvs.service.ProductService;

import javax.servlet.http.HttpSession;
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
}
