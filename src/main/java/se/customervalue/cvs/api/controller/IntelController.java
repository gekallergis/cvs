package se.customervalue.cvs.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.customervalue.cvs.api.exception.UnauthenticatedAccess;
import se.customervalue.cvs.api.exception.UnauthorizedResourceAccess;
import se.customervalue.cvs.api.representation.domain.EmployeeRepresentation;
import se.customervalue.cvs.api.representation.domain.SystemLogEntryRepresentation;
import se.customervalue.cvs.service.IntelService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class IntelController {
    @Autowired
    private HttpSession session;

    @Autowired
    private IntelService intelService;

    @RequestMapping(value = "/intel/systemlog", method = RequestMethod.GET)
    public List<SystemLogEntryRepresentation> systemLogEndpoint() throws UnauthenticatedAccess, UnauthorizedResourceAccess {
        EmployeeRepresentation currentlyLoggedInEmployee = (EmployeeRepresentation)session.getAttribute("LOGGED_IN_EMPLOYEE");
        if(currentlyLoggedInEmployee == null) {
            throw new UnauthenticatedAccess();
        }

        return intelService.getSystemLog(currentlyLoggedInEmployee);
    }
}
