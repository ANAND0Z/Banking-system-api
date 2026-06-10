package org.bank.bankingsystem.dao;


import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.model.Transaction;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionDao {

    private final SessionFactory sessionFactory;

    public void save(Transaction transaction) {

        sessionFactory
                .getCurrentSession()
                .persist(transaction);
    }

    public Optional<Transaction> findById(Long id) {

        return Optional.ofNullable(
                sessionFactory
                        .getCurrentSession()
                        .get(Transaction.class, id)
        );
    }

    public List<Transaction> findAll() {

        return sessionFactory
                .getCurrentSession()
                .createQuery(
                        "FROM Transaction",
                        Transaction.class)
                .getResultList();
    }
}