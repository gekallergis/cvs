package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.*;
import se.customervalue.cvs.abstraction.externalservice.Genny.GennyService;
import se.customervalue.cvs.abstraction.externalservice.Genny.representation.GennyRequestRepresentation;
import se.customervalue.cvs.abstraction.externalservice.Genny.representation.GennyResponseRepresentation;
import se.customervalue.cvs.api.exception.ReportGenerationException;
import se.customervalue.cvs.api.exception.UnauthorizedReportGeneration;
import se.customervalue.cvs.api.exception.UnavailableOwnedProductsException;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.GenerateReportRequestRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.ReportRepresentation;
import se.customervalue.cvs.domain.*;

import javax.transaction.Transactional;
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

	@Autowired
	private SalesDataRepository salesDataRepository;

	@Autowired
	private OwnedProductRepository ownedProductRepository;

	@Autowired
	private GennyService genny;

	@Override @Transactional
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

	@Override @Transactional
	public APIResponseRepresentation generateReport(GenerateReportRequestRepresentation report, EmployeeRepresentation loggedInEmployee) throws UnauthorizedReportGeneration, UnavailableOwnedProductsException, ReportGenerationException {
		// Logic variables set up
		boolean requesterIsAdmin = false;
		boolean requesterIsGM = false;
		boolean requesterIsCM = false;
		boolean requesterIsCE = false;

		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee generationRequester = employeeRepository.findByEmployeeId(loggedInEmployee.getEmployeeId());
		Company managedCompany = companyRepository.findByManagingEmployee(generationRequester);

		if(generationRequester.getRoles().contains(adminRole)) {
			requesterIsAdmin = true;
		} else if (managedCompany != null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				requesterIsGM = true;
			} else {
				requesterIsCM = true;
			}
		} else {
			requesterIsCE = true;
		}

		boolean salesDataBelongToRequestCompany = false;
		boolean salesDataBelongToParentCompany = false;
		boolean salesDataBelongToSubsidiary = false;

		SalesData reportSalesData = salesDataRepository.findBySalesDataId(report.getSalesDataId());
		Company reportCompany = companyRepository.findByCompanyId(report.getCompanyId());

		if(reportSalesData.getCompany().equals(reportCompany)) {
			salesDataBelongToRequestCompany = true;
		}

		if(reportSalesData.getCompany().equals(reportCompany.getParentCompany())) {
			salesDataBelongToParentCompany = true;
		}

		if(reportCompany.getSubsidiaries().size() > 0) {
			if(reportCompany.getSubsidiaries().contains(reportSalesData.getCompany())) {
				salesDataBelongToSubsidiary = true;
			}
		}

		boolean reportCompanyBelongsToRequestersGroup = false;
		boolean reportCompanyIsRequestersCompany = false;

		if(requesterIsGM) {
			List<Company> group = companyRepository.findByParentCompany(managedCompany);
			group.add(generationRequester.getEmployer());

			if(group.contains(reportCompany)) {
				reportCompanyBelongsToRequestersGroup = true;
			}
		}

		if(reportCompany.equals(generationRequester.getEmployer())) {
			reportCompanyIsRequestersCompany = true;
		}

		boolean reportCompanyIsParentCompanyOfRequestersCompany = false;

		if(!requesterIsAdmin) {
			if(reportCompany.equals(generationRequester.getEmployer().getParentCompany())) {
				reportCompanyIsParentCompanyOfRequestersCompany = true;
			}
		}

		boolean hasAvailableProducts = false;

		OwnedProduct reportOwnedProduct = ownedProductRepository.findByOwnedProductId(report.getOwnedProductId());

		if(reportOwnedProduct.getQuantity() > 0) {
			hasAvailableProducts = true;
		}

		// Business Logic
		if(!hasAvailableProducts) {
			throw new UnavailableOwnedProductsException();
		}

		if(salesDataBelongToRequestCompany || salesDataBelongToParentCompany || salesDataBelongToSubsidiary) {
			if(requesterIsAdmin) {
				// GENERATE
			} else if (requesterIsGM) {
				if(reportCompanyBelongsToRequestersGroup) {
					// GENERATE
				} else {
					throw new UnauthorizedReportGeneration();
				}
			} else if (requesterIsCM || requesterIsCE) {
				if (reportCompanyIsRequestersCompany || reportCompanyIsParentCompanyOfRequestersCompany) {
					// GENERATE
				} else {
					throw new UnauthorizedReportGeneration();
				}
			} else {
				throw new UnauthorizedReportGeneration();
			}
		} else {
			throw new UnauthorizedReportGeneration();
		}

		GennyResponseRepresentation response = genny.generate(new GennyRequestRepresentation(reportSalesData.getSalesDataId(), generationRequester.getEmployeeId(), reportCompany.getCompanyId(), reportOwnedProduct.getOwnedProductId()));
		if(response.getCode().equals("020")) {
			return new APIResponseRepresentation("020", "Report generation started successfully!");
		} else {
			throw new ReportGenerationException();
		}
	}
}
