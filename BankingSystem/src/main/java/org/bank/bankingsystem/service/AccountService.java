package org.bank.bankingsystem.service;


import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.dao.GenericDao;
import org.bank.bankingsystem.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

    private final GenericDao genericDao;

    public Account save(Account account) {
        return genericDao.create(account);
    }

    public Account update(Account account) {
        return genericDao.update(account);
    }
    public Account get(Long id) {
        return genericDao.findById(Account.class, id);
    }

    public List<Account> getAll() {
        return genericDao.findAll(Account.class);
    }

    public void delete(Long account) {
        genericDao.delete(Account.class,account);
    }
}

