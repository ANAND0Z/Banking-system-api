package org.bank.bankingsystem.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.model.Account;
import org.bank.bankingsystem.model.Transaction;
import org.bank.bankingsystem.model.TransactionStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final SessionFactory sessionFactory;

    @Transactional
    public String transfer(Long fromId, Long toId, BigDecimal amount) {

        try (Session session = sessionFactory.openSession()) {

            org.hibernate.Transaction tx = session.beginTransaction();

            try {

                Account from = session.get(Account.class, fromId);
                Account to = session.get(Account.class, toId);

                if (from == null || to == null) {
                    tx.rollback();
                    return "Account not found";
                }

                Transaction tran = new Transaction();
                tran.setFromAccount(from.getAccountNumber());
                tran.setToAccount(to.getAccountNumber());
                tran.setAmount(amount);
                tran.setStatus(TransactionStatus.PENDING);

                if (from.getBalance().compareTo(amount) < 0) {

                    tran.setStatus(TransactionStatus.FAILED);
                    session.persist(tran);

                    tx.commit();
                    return "Insufficient balance";
                }

                from.setBalance(from.getBalance().subtract(amount));
                to.setBalance(to.getBalance().add(amount));

                session.merge(from);
                session.merge(to);

                tran.setStatus(TransactionStatus.SUCCESS);
                session.persist(tran);

                tx.commit();

                return "TRANSFER SUCCESS";

            } catch (Exception e) {

                tx.rollback();
                throw e;
            }
        }
    }

    public List<Transaction> get(Long accountId) {
        try (Session session = sessionFactory.openSession()) {
            Account account = session.get(Account.class, accountId);
            return session.createQuery(
                            "from Transaction t where t.fromAccount = :accountno or t.toAccount = :accountno",
                            Transaction.class
                    )
                    .setParameter("accountno", account.getAccountNumber())
                    .getResultList();
        }
    }

    public List<Transaction> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "from Transaction",
                    Transaction.class
            ).getResultList();
        }
    }

}