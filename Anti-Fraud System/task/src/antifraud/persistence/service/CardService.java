package antifraud.persistence.service;

import antifraud.business.Card;
import antifraud.persistence.CardRepository;
import antifraud.business.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card save(Card toSave) {
        Card check = cardRepository.findByNumber(toSave.getNumber());
        if (check != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        cardRepository.save(toSave);
        return cardRepository.findByNumber(toSave.getNumber());
    }

    public Status delete(String number) {
        if (!Card.checkNumber(number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Card out = cardRepository.findByNumber(number);
        if (out == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        cardRepository.delete(out);
        return new Status(out);
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
}
