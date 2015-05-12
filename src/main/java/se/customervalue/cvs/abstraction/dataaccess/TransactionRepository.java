package se.customervalue.cvs.abstraction.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.customervalue.cvs.domain.Transaction;

import javax.transaction.Transactional;

@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {}
