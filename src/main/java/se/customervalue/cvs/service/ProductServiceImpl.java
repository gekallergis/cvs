package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.*;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.FreeProductRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.OwnedProductRepresentation;
import se.customervalue.cvs.api.representation.domain.ProductRepresentation;
import se.customervalue.cvs.domain.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private OwnedProductRepository ownedProductRepository;

	@Override @Transactional
	public List<ProductRepresentation> getProducts() {
		List<ProductRepresentation> productList = new ArrayList<ProductRepresentation>();
		List<Product> allProducts = productRepository.findAll();
		for (Product product : allProducts) {
			productList.add(new ProductRepresentation(product));
		}
		return productList;
	}

	@Override
	public List<OwnedProductRepresentation> getOwnedProducts(EmployeeRepresentation loggedInEmployee) {
		List<OwnedProduct> ownedProducts = new ArrayList<OwnedProduct>();
		List<OwnedProductRepresentation> ownedProductsRep = new ArrayList<OwnedProductRepresentation>();

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Product Service] Getting all owned products for admin user!");
			ownedProducts.addAll(ownedProductRepository.findAll());
		} else if (managedCompany != null){
			ownedProducts.addAll(ownedProductRepository.findByOwner(managedCompany));

			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				for (Company subsidiary : subsidiaries) {
					ownedProducts.addAll(ownedProductRepository.findByOwner(subsidiary));
				}
			} else {
				if(managedCompany.hasParentCompany()) {
					ownedProducts.addAll(ownedProductRepository.findByOwner(managedCompany.getParentCompany()));
				}
			}
		} else {
			ownedProducts.addAll(ownedProductRepository.findByOwner(currentEmployee.getEmployer()));
			if(currentEmployee.getEmployer().hasParentCompany()) {
				ownedProducts.addAll(ownedProductRepository.findByOwner(currentEmployee.getEmployer().getParentCompany()));
			}
		}

		for (OwnedProduct ownedProduct : ownedProducts) {
			ownedProductsRep.add(new OwnedProductRepresentation(ownedProduct));
		}

		return ownedProductsRep;
	}

	@Override @Transactional
	public APIResponseRepresentation addFreeProducts(FreeProductRepresentation freeProduct, EmployeeRepresentation loggedInEmployee) throws UnauthenticatedAccess, UnauthorizedResourceAccess, ProductNotFoundException, CompanyNotFoundException {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Product Service] Adding free products for admin user!");
			Product product = productRepository.findByProductId(freeProduct.getProductId());
			if(product == null) {
				throw new ProductNotFoundException();
			}

			Company company = companyRepository.findByCompanyId(freeProduct.getCompanyId());
			if(company == null) {
				throw new CompanyNotFoundException();
			}

			OwnedProduct existingOwnedProduct = ownedProductRepository.findByOwnerAndProduct(company, product);
			if (existingOwnedProduct != null) {
				log.debug("[Product Service] Product already exists! Updating quantity!");
				existingOwnedProduct.setQuantity(existingOwnedProduct.getQuantity() + freeProduct.getQuantity());
				ownedProductRepository.save(existingOwnedProduct);
			} else {
				OwnedProduct ownedProduct = new OwnedProduct(freeProduct.getQuantity(), company, product);
				company.getOwnedProducts().add(ownedProduct);
				product.getPurchases().add(ownedProduct);
				ownedProductRepository.save(ownedProduct);
				companyRepository.save(company);
				productRepository.save(product);
			}
			return new APIResponseRepresentation("008", "Free products added successfully!");
		}

		throw new UnauthorizedResourceAccess();
	}
}
