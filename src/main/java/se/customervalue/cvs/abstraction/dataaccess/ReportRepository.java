package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Report;

import javax.transaction.Transactional;

@Transactional
public interface ReportRepository extends JpaRepository<Report, Long> {}
