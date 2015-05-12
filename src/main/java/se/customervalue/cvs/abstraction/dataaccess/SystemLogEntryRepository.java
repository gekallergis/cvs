package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.SystemLogEntry;

import javax.transaction.Transactional;

@Transactional
public interface SystemLogEntryRepository extends JpaRepository<SystemLogEntry, Long> {}
