package org.bank.bankingsystem.service;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bank.bankingsystem.dao.AccountDao;
import org.bank.bankingsystem.dto.AccountRequest;
import org.bank.bankingsystem.exception.ResourceNotFoundException;
import org.bank.bankingsystem.model.Account;
import org.bank.bankingsystem.model.AccountStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountDao accountDao;

    public Account createAccount(
            AccountRequest request) {

        Account account = Account.builder()
                .accountNumber(
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 10))
                .customerName(
                        request.getCustomerName())
                .balance(
                        request.getBalance())
                .status(AccountStatus.ACTIVE)
                .build();

        accountDao.save(account);

        log.info(
                "Account created {}",
                account.getAccountNumber());

        return account;
    }

    public Account getAccount(Long id) {

        return accountDao.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found"));
    }

    public List<Account> getAllAccounts() {

        return accountDao.findAll();
    }

    public void deleteAccount(Long id) {

        Account account = getAccount(id);

        accountDao.delete(account);
    }
}