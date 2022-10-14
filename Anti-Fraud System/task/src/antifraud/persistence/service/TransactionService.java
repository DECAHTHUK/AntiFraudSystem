package antifraud.persistence.service;

import antifraud.business.Card;
import antifraud.business.Transaction;
import antifraud.business.dto.FeedBack;
import antifraud.persistence.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Transaction findById(long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> findAllByNumber(String number) {return transactionRepository.findAllByNumber(number);}

    public List<Transaction> findAll() {
        createConstants();
        List<Transaction> out = transactionRepository.findAll();
        out.remove(0);
        return out;
    }

    public List<Transaction> findTransactionsByNumber(String number) {
        if (!Card.checkNumber(number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        List<Transaction> transactions = findAllByNumber(number);
        if (transactions.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return transactions;
    }

    public Transaction postFeedBack(FeedBack feedBack) {
        FeedBack.toAnswer(feedBack.getFeedback());
        createConstants();
        Transaction trans = findById(feedBack.getTransactionId());
        if (trans == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if (trans.getFeedback() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        String validity = trans.getResult();
        String ourFeedback = feedBack.getFeedback();

        trans.setFeedback(ourFeedback);
        save(trans);
        if (validity.equals(ourFeedback)) {
            trans.setFeedback("");
            save(trans);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (validity.equals("ALLOWED")) {
            updateAllowed(trans.getAmount(), false);
            if (ourFeedback.equals("PROHIBITED")) {
                updateManual(trans.getAmount(), false);
            }
        } else if (validity.equals("MANUAL_PROCESSING")) {
            if (ourFeedback.equals("ALLOWED")) {
                updateAllowed(trans.getAmount(), true);
            } else {
                updateManual(trans.getAmount(), false);
            }
        } else {
            updateManual(trans.getAmount(), true);
            if (ourFeedback.equals("ALLOWED")) {
                updateAllowed(trans.getAmount(), true);
            }
        }
        System.out.println(trans);
        return trans;
    }

    public void createConstants() {
        if (findById(3) == null) {
            Transaction maxes = new Transaction();
            maxes.setRegion("200");
            maxes.setDate("1500");
            transactionRepository.save(maxes);
        }
    }

    public Pair<Integer, Integer> getConstants() {
        createConstants();
        Transaction maxes = findById(3);
        return Pair.of(Integer.parseInt(maxes.getRegion()), Integer.parseInt(maxes.getDate()));
    }

    public void updateAllowed(long value, boolean increase) {
        createConstants();
        Transaction maxes = findById(3);
        Pair<Integer, Integer> consts = getConstants();
        int maxAllowed;
        if (increase) {
            maxAllowed = (int) Math.ceil(consts.getFirst() * 0.8 + 0.2 * value);
        } else {
            maxAllowed = (int) Math.ceil(consts.getFirst() * 0.8 - 0.2 * value);
        }
        maxes.setRegion(String.valueOf(maxAllowed));
        transactionRepository.save(maxes);
    }

    public void updateManual(long value, boolean increase) {
        createConstants();
        Transaction maxes = findById(3);
        Pair<Integer, Integer> consts = getConstants();
        int maxManual;
        if (increase) {
            maxManual = (int) Math.ceil(consts.getSecond() * 0.8 + 0.2 * value);
        } else {
            maxManual = (int) Math.ceil(consts.getSecond() * 0.8 - 0.2 * value);
        }
        maxes.setDate(String.valueOf(maxManual));
        transactionRepository.save(maxes);
    }
}
