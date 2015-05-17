package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Employee;
import se.customervalue.cvs.domain.Role;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByLabel(String label);
	Role findByRoleId(int roleId);
}
