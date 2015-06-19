package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.*;
import se.customervalue.cvs.api.exception.InvoiceNotFoundException;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.InvoiceRepresentation;
import se.customervalue.cvs.domain.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private OrderHeaderRepository orderHeaderRepository;

	@Override @Transactional
	public List<InvoiceRepresentation> getInvoices(EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess {
		List<Invoice> invoices = new ArrayList<Invoice>();

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(!currentEmployee.getRoles().contains(adminRole) && managedCompany == null) {
			throw new UnauthorizedResourceAccess();
		}

		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Invoice Service] Retrieving all invoices for admin user!");
			invoices = invoiceRepository.findAll();
		} else {
			if(managedCompany != null) {
				invoices.addAll(getInvoicesForCompany(currentEmployee.getEmployer()));

				List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
				if(subsidiaries.size() > 0) {
					for (Company subsidiary : subsidiaries) {
						invoices.addAll(getInvoicesForCompany(subsidiary));
					}
				}
			}
		}

		List<InvoiceRepresentation> invoicesRep = new ArrayList<InvoiceRepresentation>();
		for (Invoice invoice : invoices) {
			invoicesRep.add(new InvoiceRepresentation(invoice));
		}
		return invoicesRep;
	}

	private List<Invoice> getInvoicesForCompany(Company company) {
		List<Invoice> invoices = new ArrayList<Invoice>();
		List<OrderHeader> orders = new ArrayList<OrderHeader>();

		orders.addAll(orderHeaderRepository.findByPurchasedFor(company));
		for (OrderHeader order : orders) {
			if(order.getPurchasedFor().equals(company)) {
				invoices.add(order.getInvoice());
			}
		}

		return invoices;
	}

	@Override @Transactional
	public InvoiceRepresentation getInvoice(int invoiceId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, InvoiceNotFoundException {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(!currentEmployee.getRoles().contains(adminRole) && managedCompany == null) {
			throw new UnauthorizedResourceAccess();
		}

		Invoice requestedInvoice = invoiceRepository.findByInvoiceId(invoiceId);
		if(requestedInvoice == null) {
			throw new InvoiceNotFoundException();
		}

		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Invoice Service] Retrieving invoice " + invoiceId + " for admin user!");
		} else {
			if(managedCompany != null) {
				Company invoiceCompany = requestedInvoice.getOrder().getPurchasedFor();
				List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
				if(subsidiaries.size() > 0) {
					if(!subsidiaries.contains(invoiceCompany) && !managedCompany.equals(invoiceCompany)) {
						throw new UnauthorizedResourceAccess();
					}
				} else {
					if(!managedCompany.equals(invoiceCompany)) {
						throw new UnauthorizedResourceAccess();
					}
				}
			}
		}

		return new InvoiceRepresentation(requestedInvoice);
	}

	@Override @Transactional
	public APIResponseRepresentation settleInvoice(int invoiceId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, InvoiceNotFoundException {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		if(!currentEmployee.getRoles().contains(adminRole)) {
			throw new UnauthorizedResourceAccess();
		}

		Invoice requestedInvoice = invoiceRepository.findByInvoiceId(invoiceId);
		if(requestedInvoice == null) {
			throw new InvoiceNotFoundException();
		}

		requestedInvoice.setStatus(InvoiceStatus.PAID);
		invoiceRepository.save(requestedInvoice);

		return new APIResponseRepresentation("017", "The invoice status has been set to paid!");
	}
}
