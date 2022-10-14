package antifraud.business.mappers;

import antifraud.business.Card;
import antifraud.business.dto.CardDto;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {
    public Card toCard(CardDto cardDto) {
        Card card = new Card(cardDto.getNumber());
        return card;
    }
}
