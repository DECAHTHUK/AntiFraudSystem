package antifraud.persistence;

import antifraud.business.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String number);

    List<Card> findAll();
}
