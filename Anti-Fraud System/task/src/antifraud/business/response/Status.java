package antifraud.business.response;

import antifraud.business.Card;
import antifraud.business.Ip;
import antifraud.business.User;
import lombok.Data;

@Data
public class Status {
    private String status;

    public Status(User user) {
        String action = user.isLocked() ? "locked!" : "unlocked!";
        this.status = "User " + user.getUsername() + " " + action;
    }

    public Status(Card card) {
        this.status = "Card " + card.getNumber() + " successfully removed!";
    }
    public Status(Ip ip) {
        this.status = "IP " + ip.getIp() + " successfully removed!";
    }
}
