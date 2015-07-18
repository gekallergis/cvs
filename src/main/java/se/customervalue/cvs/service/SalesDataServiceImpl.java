package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.customervalue.cvs.abstraction.dataaccess.CompanyRepository;
import se.customervalue.cvs.abstraction.dataaccess.EmployeeRepository;
import se.customervalue.cvs.abstraction.dataaccess.RoleRepository;
import se.customervalue.cvs.abstraction.dataaccess.SalesDataRepository;
import se.customervalue.cvs.api.exception.*;
import se.customervalue.cvs.api.representation.APIResponseRepresentation;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SalesDataRepresentation;
import se.customervalue.cvs.common.CVSConfig;
import se.customervalue.cvs.domain.*;
import se.customervalue.cvs.service.SalesDataImport.SalesDataImportService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
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

	@Autowired
	private SalesDataImportService salesDataImportService;

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

	@Override @Transactional
	public APIResponseRepresentation uploadSalesData(MultipartFile salesData, int companyId, String month, String year, EmployeeRepresentation loggedInEmployee) throws InvalidEmployeeCompanyCombinationException, FoundActiveProcessingException, SalesDataUploadException {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		Company company = companyRepository.findByCompanyId(companyId);
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Sales Data Service] Uploading sales data for admin user!");
		} else if (managedCompany != null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				if(!subsidiaries.contains(company) && !managedCompany.equals(company)) {
					throw new InvalidEmployeeCompanyCombinationException();
				}
			} else {
				if(!managedCompany.equals(company)) {
					throw new InvalidEmployeeCompanyCombinationException();
				}
			}
		} else {
			if(!currentEmployee.getEmployer().equals(company)) {
				throw new InvalidEmployeeCompanyCombinationException();
			}
		}

		List<SalesData> companySalesDataProcessing = salesDataRepository.findByCompanyAndSalesPeriodAndStatus(company, CVSConfig.months[Integer.parseInt(month)] + " " + year, SalesDataStatus.PROCESSING);
		if(companySalesDataProcessing.size() > 0) {
			throw new FoundActiveProcessingException();
		}

		SalesData newSalesData = null;
		try {
			// Create new entry
			newSalesData = new SalesData(CVSConfig.months[Integer.parseInt(month)] + " " + year, SalesDataStatus.PROCESSING, new Date(), "");
			newSalesData.setUploader(currentEmployee);
			currentEmployee .getSalesDataUploads().add(newSalesData);
			newSalesData.setCompany(company);
			company.getSalesData().add(newSalesData);
			salesDataRepository.save(newSalesData);

			/// Upload file
			String filename = newSalesData.getSalesDataId() + ".txt";
			String directory = CVSConfig.SALES_DATA_FS_STORE;
			String filepath = Paths.get(directory, filename).toString();
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(salesData.getBytes());
			stream.close();

			newSalesData.setFilePath(filepath);

			List<SalesData> companySalesDataChecked = salesDataRepository.findByCompanyAndSalesPeriodAndStatus(company, CVSConfig.months[Integer.parseInt(month)] + " " + year, SalesDataStatus.CHECKED);
			if (companySalesDataChecked.size() == 1) {
				companySalesDataChecked.get(0).setStatus(SalesDataStatus.REPLACED);
			}

			salesDataImportService.start(new File(filepath), newSalesData.getSalesDataId());
		} catch (IOException ex) {
			salesDataRepository.delete(newSalesData);
			throw new SalesDataUploadException();
		}

		return new APIResponseRepresentation("018", "File uploaded successfully!");
	}

	@Override @Transactional
	public APIResponseRepresentation deleteSalesData(int salesDataId, EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess, SalesDataDeleteException {
		Role adminRole = roleRepository.findByLabel("isAdmin");
		Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
		Company managedCompany = companyRepository.findByManagingEmployee(currentEmployee);
		SalesData deleteSalesData = salesDataRepository.findBySalesDataId(salesDataId);
		if(currentEmployee.getRoles().contains(adminRole)) {
			log.debug("[Sales Data Service] Deleting sales data for admin user!");
		} else if (managedCompany != null) {
			List<Company> subsidiaries = companyRepository.findByParentCompany(managedCompany);
			if(subsidiaries.size() > 0) {
				if(!subsidiaries.contains(deleteSalesData.getCompany()) && !managedCompany.equals(deleteSalesData.getCompany())) {
					throw new UnauthorizedResourceAccess();
				}
			} else {
				if(!managedCompany.equals(deleteSalesData.getCompany())) {
					throw new UnauthorizedResourceAccess();
				}
			}
		} else {
			if(!currentEmployee.getEmployer().equals(deleteSalesData.getCompany())) {
				throw new UnauthorizedResourceAccess();
			}
		}

		try {
			String filename = deleteSalesData.getSalesDataId() + ".txt";
			String directory = CVSConfig.SALES_DATA_FS_STORE;
			Files.delete(Paths.get(directory, filename));
		} catch (IOException ex) {
			throw new SalesDataDeleteException();
		} finally {
			deleteSalesData.setStatus(SalesDataStatus.DELETED);
			salesDataRepository.save(deleteSalesData);
		}

		return new APIResponseRepresentation("019", "Sales data marked as deleted! All files were successfully removed!");
	}
}
