package se.customervalue.cvs.service;

import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SystemLogEntryRepresentation;

import java.util.List;

public interface IntelService {
    List<SystemLogEntryRepresentation> getSystemLog(EmployeeRepresentation loggedInEmployee) throws UnauthorizedResourceAccess;
}
