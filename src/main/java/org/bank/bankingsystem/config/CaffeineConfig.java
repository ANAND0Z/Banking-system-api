package org.bank.bankingsystem.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bank.bankingsystem.model.Account;
import org.bank.bankingsystem.model.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {

    @Bean
    public Cache<Long, List<Transaction>> transactionCache() {

        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public Cache<Long, Account> accountSummaryCache() {

        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(
                        5,
                        TimeUnit.MINUTES)
                .build();
    }
}