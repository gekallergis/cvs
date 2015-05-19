package se.customervalue.cvs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.customervalue.cvs.abstraction.dataaccess.EmployeeRepository;
import se.customervalue.cvs.abstraction.dataaccess.RoleRepository;
import se.customervalue.cvs.abstraction.dataaccess.SystemLogEntryRepository;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SystemLogEntryRepresentation;
import se.customervalue.cvs.domain.Employee;
import se.customervalue.cvs.domain.Role;
import se.customervalue.cvs.domain.SystemLogEntry;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntelServiceImpl implements IntelService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SystemLogEntryRepository systemLogEntryRepository;

    @Override
    public List<SystemLogEntryRepresentation> getSystemLog(EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess {
        Role adminRole = roleRepository.findByLabel("isAdmin");
        Employee currentEmployee = employeeRepository.findByEmail(loggedInEmployee.getEmail());
        if(currentEmployee.getRoles().contains(adminRole)) {
            log.debug("[Intel Service] Retrieving system log for admin user!");
            List<SystemLogEntryRepresentation> systemLogList = new ArrayList<SystemLogEntryRepresentation>();
            List<SystemLogEntry> allSystemLogEntries = systemLogEntryRepository.findAll();
            for (SystemLogEntry systemLogEntry : allSystemLogEntries) {
                systemLogList.add(new SystemLogEntryRepresentation(systemLogEntry));
            }
            return systemLogList;
        }

        throw new UnauthorizedResourceAccess();
    }
}
