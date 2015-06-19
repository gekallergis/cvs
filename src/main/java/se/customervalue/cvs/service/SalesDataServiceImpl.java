package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.CompanyRepository;
import se.customervalue.cvs.abstraction.dataaccess.EmployeeRepository;
import se.customervalue.cvs.abstraction.dataaccess.RoleRepository;
import se.customervalue.cvs.abstraction.dataaccess.SalesDataRepository;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SalesDataRepresentation;
import se.customervalue.cvs.domain.Company;
import se.customervalue.cvs.domain.Employee;
import se.customervalue.cvs.domain.Role;
import se.customervalue.cvs.domain.SalesData;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesDataServiceImpl implements SalesDataService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SalesDataRepository salesDataRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Override @Transactional
	public List<SalesDataRepresentation> getSalesData(EmployeeRepresentation loggedInEmployee) {
		List<SalesData> salesData = new ArrayList<SalesData>();
		List<SalesDataRepresentation> salesDataRep = new ArrayList<SalesDataRepresentation>();

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Sales Data Service] Getting all sales data for admin user!");
			salesData.addAll(salesDataRepository.findAll());
		} else if (managedCompany != null){
			salesData.addAll(salesDataRepository.findByCompany(managedCompany));

			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				for (Company subsidiary : subsidiaries) {
					salesData.addAll(salesDataRepository.findByCompany(subsidiary));
				}
			} else {
				if(managedCompany.hasParentCompany()) {
					salesData.addAll(salesDataRepository.findByCompany(managedCompany.getParentCompany()));
				}
			}
		} else {
			salesData.addAll(salesDataRepository.findByCompany(currentEmployee.getEmployer()));
			if(currentEmployee.getEmployer().hasParentCompany()) {
				salesData.addAll(salesDataRepository.findByCompany(currentEmployee.getEmployer().getParentCompany()));
			}
		}

		for (SalesData salesDatum : salesData) {
			salesDataRep.add(new SalesDataRepresentation(salesDatum));
		}

		return salesDataRep;
	}
}
