package antifraud.business.mappers;

import antifraud.business.Transaction;
import antifraud.business.dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transaction toTransaction(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setIp(transactionDto.getIp());
        transaction.setNumber(transactionDto.getNumber());
        transaction.setRegion(transactionDto.getRegion());
        transaction.setDate(transactionDto.getDate());
        return transaction;
    }
}
