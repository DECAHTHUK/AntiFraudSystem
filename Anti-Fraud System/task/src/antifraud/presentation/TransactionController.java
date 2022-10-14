package antifraud.presentation;

import antifraud.business.Card;
import antifraud.business.Ip;
import antifraud.business.dto.CardDto;
import antifraud.business.dto.FeedBack;
import antifraud.business.dto.TransactionDto;
import antifraud.business.mappers.CardMapper;
import antifraud.business.mappers.TransactionMapper;
import antifraud.persistence.service.CardService;
import antifraud.persistence.service.IpService;
import antifraud.business.Transaction;
import antifraud.business.dto.IpDto;
import antifraud.business.mappers.IpMapper;
import antifraud.business.response.Result;
import antifraud.business.response.Status;
import antifraud.persistence.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    IpService ipService;

    @Autowired
    IpMapper ipMapper;

    @Autowired
    CardService cardService;

    @Autowired
    CardMapper cardMapper;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    TransactionService transactionService;

    private List<Long> trans = new ArrayList<>();

    @PostMapping(value = "/api/antifraud/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result postTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionMapper.toTransaction(transactionDto);
        Result result = transaction.getResult(ipService.findByIp(transaction.getIp()),
                cardService.findByNumber(transaction.getNumber()),
                transactionService.findAllByNumber(transaction.getNumber()), transactionService.getConstants());
        transactionService.save(transaction);
        return result;
    }

    @PutMapping(value = "/api/antifraud/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaction postFeedback(@Valid @RequestBody FeedBack feedBack) {
        return transactionService.postFeedBack(feedBack);
    }

    @PostMapping("/api/antifraud/suspicious-ip")
    public Ip postSuspiciousIp(@Valid @RequestBody IpDto ipDto) {
        return ipService.save(ipMapper.toIp(ipDto));
    }

    @DeleteMapping(value = "/api/antifraud/suspicious-ip/{ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Status deleteSuspiciousIp(@PathVariable String ip) {
        IpDto.validate(ip);
        return ipService.delete(ip);
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    public String getSuspiciousIps() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ipService.findAll());
    }

    @PostMapping(value = "/api/antifraud/stolencard", produces = MediaType.APPLICATION_JSON_VALUE)
    public Card postStolenCard(@Valid @RequestBody CardDto cardDto) {
        return cardService.save(cardMapper.toCard(cardDto));
    }

    @DeleteMapping(value = "/api/antifraud/stolencard/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Status deleteStolenCard(@PathVariable String number) {
        return cardService.delete(number);
    }

    @GetMapping("/api/antifraud/stolencard")
    public String getStolenCards() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(cardService.findAll());
    }

    @GetMapping(value = "/api/antifraud/history")
    public String transactionHistory() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(transactionService.findAll());
    }

    @GetMapping(value = "/api/antifraud/history/{number}")
    public String transactionsHistoryOfCard(@PathVariable String number) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(transactionService.findTransactionsByNumber(number));
    }
}
