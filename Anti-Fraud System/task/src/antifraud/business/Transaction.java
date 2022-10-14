package antifraud.business;

import antifraud.business.response.Answer;
import antifraud.business.response.Result;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("transactionId")
    private long id;

    private long amount;

    private String ip;

    private String number;

    private String region;

    private String date;

    private String result;

    @JsonProperty("feedback")
    public String getFeedbackString() {
        return feedback == null ? "" : feedback;
    }
    private String feedback;

    public Result getResult(Ip ip, Card number, List<Transaction> transactions, Pair<Integer, Integer> constants) {
        if (!Card.checkNumber(this.number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String info = "";
        if (this.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Pair<Integer, Integer> pair = reviewNumbers(transactions);
        info = checkProhibited(ip, number, pair, constants);
        if (info.length() != 0) {
            this.result = "PROHIBITED";
            return new Result(Answer.PROHIBITED, info);
        }
        info = checkManual(pair, constants);
        if (info.length() != 0) {
            this.result = "MANUAL_PROCESSING";
            return new Result(Answer.MANUAL_PROCESSING, info);
        }
        this.result = "ALLOWED";
        return new Result(Answer.ALLOWED, "none");
    }

    public String checkProhibited(Ip ip, Card number, Pair<Integer, Integer> pair, Pair<Integer, Integer> constants) {
        String out = "";
        int maxManual = constants.getSecond();
        if (this.amount > maxManual) {
            out += "amount";
        }
        if (number != null) {
            if (out.length() != 0) {
                out += ", card-number";
            } else {
                out += "card-number";
            }
        }
        if (ip != null) {
            if (out.length() != 0) {
                out += ", ip";
            } else {
                out += "ip";
            }
        }
        if (pair.getFirst() > 3) {
            if (out.length() != 0) {
                out += ", ip-correlation";
            } else {
                out += "ip-correlation";
            }
        }
        if (pair.getSecond() > 3) {
            if (out.length() != 0) {
                out += ", region-correlation";
            } else {
                out += "region-correlation";
            }
        }
        return out;
    }

    public String checkManual(Pair<Integer, Integer> pair, Pair<Integer, Integer> constants) {
        String out = "";
        int maxAllowed = constants.getFirst();
        int maxManual = constants.getSecond();
        if (this.amount <= maxManual && this.amount > maxAllowed) {
            out += "amount";
        }
        if (pair.getFirst() == 3) {
            if (out.length() != 0) {
                out += ", ip-correlation";
            } else {
                out += "ip-correlation";
            }
        }
        if (pair.getSecond() == 3) {
            if (out.length() != 0) {
                out += ", region-correlation";
            } else {
                out += "region-correlation";
            }
        }
        return out;
    }

    public Pair<Integer, Integer> reviewNumbers(List<Transaction> transactions) {
        ArrayList<String> regionsList = new ArrayList<>();
        regionsList.add(this.region);
        ArrayList<String> ipsList = new ArrayList<>();
        ipsList.add(this.ip);
        LocalDateTime timeNow = LocalDateTime.parse(this.date);
        for (Transaction transaction : transactions) {
            LocalDateTime timeThen = LocalDateTime.parse(transaction.getDate());
            long diff = ChronoUnit.SECONDS.between(timeThen, timeNow);
            if (diff <= 3600 && diff > 0) {
                if (!regionsList.contains(transaction.getRegion())) {
                    regionsList.add(transaction.getRegion());
                }
                if (!ipsList.contains(transaction.getIp())) {
                    ipsList.add(transaction.getIp());
                }
            }
        }
        return Pair.of(ipsList.size(), regionsList.size());
    }
}
