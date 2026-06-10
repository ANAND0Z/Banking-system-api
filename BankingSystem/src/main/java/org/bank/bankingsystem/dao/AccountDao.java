package org.bank.bankingsystem.dao;


import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.model.Account;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountDao {

    private final SessionFactory sessionFactory;

    public void save(Account account) {
        sessionFactory.getCurrentSession().persist(account);
    }

    public Account update(Account account) {
        return (Account) sessionFactory
                .getCurrentSession()
                .merge(account);
    }

    public Optional<Account> findById(Long id) {

        return Optional.ofNullable(
                sessionFactory
                        .getCurrentSession()
                        .get(Account.class, id)
        );
    }

    public Optional<Account> findByAccountNumber(
            String accountNumber) {

        return sessionFactory
                .getCurrentSession()
                .createQuery(
                        "FROM Account WHERE accountNumber=:accNo",
                        Account.class)
                .setParameter("accNo", accountNumber)
                .uniqueResultOptional();
    }

    public List<Account> findAll() {

        return sessionFactory
                .getCurrentSession()
                .createQuery(
                        "FROM Account",
                        Account.class)
                .getResultList();
    }

    public void delete(Account account) {

        sessionFactory
                .getCurrentSession()
                .remove(account);
    }
}