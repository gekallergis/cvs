package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.CompanyRepository;
import se.customervalue.cvs.abstraction.dataaccess.EmployeeRepository;
import se.customervalue.cvs.abstraction.dataaccess.ReportRepository;
import se.customervalue.cvs.abstraction.dataaccess.RoleRepository;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.ReportRepresentation;
import se.customervalue.cvs.domain.Company;
import se.customervalue.cvs.domain.Employee;
import se.customervalue.cvs.domain.Report;
import se.customervalue.cvs.domain.Role;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public List<ReportRepresentation> getReports(EmployeeRepresentation loggedInEmployee) {
		List<Report> reports = new ArrayList<Report>();
		List<ReportRepresentation> reportsRep = new ArrayList<ReportRepresentation>();

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Sales Data Service] Getting all reports for admin user!");
			reports.addAll(reportRepository.findAll());
		} else if (managedCompany != null){
			reports.addAll(reportRepository.findByCompany(managedCompany));

			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				for (Company subsidiary : subsidiaries) {
					reports.addAll(reportRepository.findByCompany(subsidiary));
				}
			} else {
				if(managedCompany.hasParentCompany()) {
					reports.addAll(reportRepository.findByCompany(managedCompany.getParentCompany()));
				}
			}
		} else {
			reports.addAll(reportRepository.findByCompany(currentEmployee.getEmployer()));
			if(currentEmployee.getEmployer().hasParentCompany()) {
				reports.addAll(reportRepository.findByCompany(currentEmployee.getEmployer().getParentCompany()));
			}
		}

		for (Report report : reports) {
			reportsRep.add(new ReportRepresentation(report));
		}

		return reportsRep;
	}
}
