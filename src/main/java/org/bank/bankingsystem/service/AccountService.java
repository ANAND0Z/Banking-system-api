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
    private final RedisCacheService redisCacheService;
    private final LruCache<Long, Account> lruCache;
    private final com.github.benmanes.caffeine.cache.Cache<Long, Account> caffeineCache;

    private static final String ACCOUNT_KEY = "account:";


    public Account save(Account account) {

        Account saved = genericDao.create(account);

        String key = ACCOUNT_KEY + saved.getId();

        redisCacheService.put(key, saved);
        lruCache.put(saved.getId(), saved);
        caffeineCache.put(saved.getId(), saved);

        return saved;
    }


    public Account update(Account account) {

        Account updated = genericDao.update(account);

        String key = ACCOUNT_KEY + updated.getId();

        redisCacheService.delete(key);
        lruCache.remove(updated.getId());
        caffeineCache.invalidate(updated.getId());

        redisCacheService.put(key, updated);
        lruCache.put(updated.getId(), updated);
        caffeineCache.put(updated.getId(), updated);

        return updated;
    }


    public Account get(Long id) {

        String key = ACCOUNT_KEY + id;

        Account account = redisCacheService.get(key, Account.class);
        if (account != null) {
            return account;
        }

        account = lruCache.get(id);
        if (account != null) {
            redisCacheService.put(key, account);
            return account;
        }

        account = caffeineCache.getIfPresent(id);
        if (account != null) {
            redisCacheService.put(key, account);
            lruCache.put(id, account);
            return account;
        }

        account = genericDao.findById(Account.class, id);

        if (account != null) {
            redisCacheService.put(key, account);
            lruCache.put(id, account);
            caffeineCache.put(id, account);
        }

        return account;
    }


    public List<Account> getAll() {
        return genericDao.findAll(Account.class);
    }


    public void delete(Long id) {

        genericDao.delete(Account.class, id);

        String key = ACCOUNT_KEY + id;

        redisCacheService.delete(key);
        lruCache.remove(id);
        caffeineCache.invalidate(id);
    }
}

