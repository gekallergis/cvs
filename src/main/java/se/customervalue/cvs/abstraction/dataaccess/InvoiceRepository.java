package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Invoice;

import javax.transaction.Transactional;

@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	Invoice findByInvoiceId(int invoiceId);
}
