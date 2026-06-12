package org.bank.bankingsystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final SessionFactory sessionFactory;
    private final KafkaProducerService kafkaProducerService;

    private final RedisCacheService redisCacheService;
    private final com.github.benmanes.caffeine.cache.Cache<Long, List<Transaction>> caffeineCache;
    private final LruCache<Long, List<Transaction>> lruCache;

    private static final String KEY = "transaction:";

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


                TransactionEvent event = TransactionEvent.builder()
                        .transactionId(tran.getId())
                        .fromAccount(tran.getFromAccount())
                        .toAccount(tran.getToAccount())
                        .amount(tran.getAmount())
                        .eventTime(LocalDateTime.now())
                        .build();


                kafkaProducerService.publish(event);

                evictCache(fromId);
                evictCache(toId);

                return "TRANSFER SUCCESS";

            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }


    public List<Transaction> get(Long accountId) {

        String key = KEY + accountId;

        List<Transaction> redis = (List<Transaction>) redisCacheService.getList(key,Transaction.class);
        if (redis != null) {
            caffeineCache.put(accountId, redis);
            lruCache.put(accountId, redis);
            return redis;
        }

        List<Transaction> caffeine = caffeineCache.getIfPresent(accountId);
        if (caffeine != null) {
            redisCacheService.put(key, caffeine);
            lruCache.put(accountId, caffeine);
            return caffeine;
        }

        List<Transaction> lru = lruCache.get(accountId);
        if (lru != null) {
            redisCacheService.put(key, lru);
            caffeineCache.put(accountId, lru);
            return lru;
        }

        try (Session session = sessionFactory.openSession()) {

            Account account = session.get(Account.class, accountId);

            List<Transaction> result = session.createQuery(
                            "from Transaction t where t.fromAccount = :acc or t.toAccount = :acc",
                            Transaction.class
                    )
                    .setParameter("acc", account.getAccountNumber())
                    .getResultList();

            redisCacheService.put(key, result);
            caffeineCache.put(accountId, result);
            lruCache.put(accountId, result);

            return result;
        }
    }

    public List<Transaction> getAll() {

        String key = KEY + "all";

        List<Transaction> redis = (List<Transaction>) redisCacheService.getList(key,Transaction.class);
        if (redis != null) return redis;

        try (Session session = sessionFactory.openSession()) {

            List<Transaction> result =
                    session.createQuery("from Transaction", Transaction.class)
                            .getResultList();

            redisCacheService.put(key, result);
            session.close();
            return result;
        }
    }


    public List<TransactionHistory> transactionhistory(){
        try (Session session = sessionFactory.openSession()) {

            List<TransactionHistory> result =
                    session.createQuery("from TransactionHistory", TransactionHistory.class)
                            .getResultList();

            return result;
        }
    }

    private void evictCache(Long accountId) {

        String key = KEY + accountId;

        redisCacheService.delete(key);
        caffeineCache.invalidate(accountId);
        lruCache.remove(accountId);
    }
}