package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.*;
import se.customervalue.cvs.api.exception.ProductNotFoundException;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.exception.UnpaidInvoiceQuotaReached;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.OrderHeaderRepresentation;
import se.customervalue.cvs.api.representation.domain.OrderItemRepresentation;
import se.customervalue.cvs.common.CVSConfig;
import se.customervalue.cvs.domain.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private OrderHeaderRepository orderHeaderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OwnedProductRepository ownedProductRepository;

	@Override @Transactional
	public APIResponseRepresentation placeOrder(OrderHeaderRepresentation order, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, ProductNotFoundException, UnpaidInvoiceQuotaReached {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee purchasedBy = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company purchasedFor = companyRepository.findByCompanyId(order.getPurchasedFor().getCompanyId());
		Company managedCompany = companyRepository.findByManagingEmployee(purchasedBy);
		if(!purchasedBy.getRoles().contains(adminRole) && managedCompany == null) {
			throw new UnauthorizedResourceAccess();
		}

		for (OrderItemRepresentation orderItem : order.getItems()) {
			Product productItem = productRepository.findByProductId(orderItem.getProductId());
			if(productItem == null) {
				throw new ProductNotFoundException();
			}
		}

		float totalOwed = 0.0f;
		List<OrderHeader> companyOrders = purchasedFor.getOrders();
		for (OrderHeader orderHeader : companyOrders) {
			if(orderHeader.getInvoice().getStatus() == InvoiceStatus.UNPAID) {
				totalOwed += calculateTotalCost(orderHeader.getInvoice());
			}
		}

		log.debug("[Order Service] " + purchasedFor.getName() + "owes " + totalOwed + "SEK");

		if(totalOwed > purchasedFor.getInvoiceLimit()) {
			throw new UnpaidInvoiceQuotaReached();
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, CVSConfig.INVOICE_DUE_DATE_DAYS_AFTER_PURCHASE);

		Invoice newInvoice = new Invoice("AA-454-4567-00", cal.getTime(), CVSConfig.DEFAULT_VAT, InvoiceStatus.UNPAID);
		OrderHeader newOrder = new OrderHeader(new Date());

		newOrder.setPurchasedBy(purchasedBy);
		newOrder.setPurchasedFor(purchasedFor);
		purchasedFor.getOrders().add(newOrder);
		purchasedBy.getOrders().add(newOrder);
		newOrder.setInvoice(newInvoice);
		newInvoice.setOrder(newOrder);

		invoiceRepository.save(newInvoice);
		orderHeaderRepository.save(newOrder);

		for (OrderItemRepresentation orderItemRepresentation : order.getItems()) {
			OrderItem newOrderItem = new OrderItem(orderItemRepresentation.getName(), orderItemRepresentation.getInfo(), orderItemRepresentation.getQuantity(), orderItemRepresentation.getUnitPrice());
			newOrderItem.setOrder(newOrder);
			newOrder.getOrderItems().add(newOrderItem);
			orderItemRepository.save(newOrderItem);
		}

		for (OrderItemRepresentation orderItem : order.getItems()) {
			Product productItem = productRepository.findByProductId(orderItem.getProductId());
			OwnedProduct alreadyOwnedProduct = ownedProductRepository.findByOwnerAndProduct(purchasedFor, productItem);
			if(alreadyOwnedProduct == null) {
				OwnedProduct newOwnedProduct = new OwnedProduct(orderItem.getQuantity(), purchasedFor, productItem);
				purchasedFor.getOwnedProducts().add(newOwnedProduct);
				productItem.getPurchases().add(newOwnedProduct);
				productRepository.save(productItem);
				ownedProductRepository.save(newOwnedProduct);
			} else {
				alreadyOwnedProduct.setQuantity(alreadyOwnedProduct.getQuantity() + orderItem.getQuantity());
				ownedProductRepository.save(alreadyOwnedProduct);
			}
		}
		companyRepository.save(purchasedFor);
		employeeRepository.save(purchasedBy);

		return new APIResponseRepresentation("015", "You order has been placed, you can see the invoice issued for you here!");
	}

	private float calculateTotalCost(OrderHeader order) {
		float totalCost = 0.0f;

		for (OrderItem item : order.getOrderItems()) {
			totalCost += item.getQuantity() * item.getUnitPrice();
		}

		return totalCost;
	}

	private float calculateTotalCost(Invoice invoice) {
		float orderCost = calculateTotalCost(invoice.getOrder());
		return orderCost + ((orderCost * invoice.getVAT()) / 100.0f);
	}
}
