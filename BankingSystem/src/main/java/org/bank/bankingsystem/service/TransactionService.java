package org.bank.bankingsystem.service;




import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bank.bankingsystem.dao.AccountDao;
import org.bank.bankingsystem.dao.TransactionDao;
import org.bank.bankingsystem.dto.TransferRequest;
import org.bank.bankingsystem.exception.InsufficientBalanceException;
import org.bank.bankingsystem.exception.ResourceNotFoundException;
import org.bank.bankingsystem.model.Account;
import org.bank.bankingsystem.model.Transaction;
import org.bank.bankingsystem.model.TransactionStatus;
import org.bank.bankingsystem.model.TransactionType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public Transaction transfer(
            TransferRequest request) {

        Account source =
                accountDao.findById(
                                request.getSourceAccountId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Source account not found"));

        Account target =
                accountDao.findById(
                                request.getTargetAccountId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Target account not found"));

        if (source.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        source.setBalance(
                source.getBalance()
                        .subtract(request.getAmount()));

        target.setBalance(
                target.getBalance()
                        .add(request.getAmount()));

        accountDao.update(source);
        accountDao.update(target);

        Transaction transaction =
                Transaction.builder()
                        .sourceAccountId(source.getId())
                        .targetAccountId(target.getId())
                        .amount(request.getAmount())
                        .transactionType(
                                TransactionType.TRANSFER)
                        .transactionStatus(
                                TransactionStatus.COMPLETED)
                        .build();

        transactionDao.save(transaction);

        log.info(
                "Transfer completed from {} to {} amount {}",
                source.getId(),
                target.getId(),
                request.getAmount());

        return transaction;
    }
}