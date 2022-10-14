package antifraud.persistence;

import antifraud.business.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAll();

    Transaction findById(long id);

    List<Transaction> findAllByNumber(String number);
}
