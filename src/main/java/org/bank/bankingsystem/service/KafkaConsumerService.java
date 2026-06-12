package org.bank.bankingsystem.service;


import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.dao.GenericDao;
import org.bank.bankingsystem.model.TransactionEvent;
import org.bank.bankingsystem.model.TransactionHistory;
import org.bank.bankingsystem.model.TransactionStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final GenericDao genericDao;

    @KafkaListener(
            topics = "bank-transactions",
            groupId = "bank-group")
    public void consume(TransactionEvent event) {

        TransactionHistory history =
                TransactionHistory.builder()
                        .transactionId(event.getTransactionId())
                        .fromAccount(event.getFromAccount())
                        .toAccount(event.getToAccount())
                        .amount(event.getAmount())
                        .status(TransactionStatus.SUCCESS)
                        .eventTime(event.getEventTime())
                        .consumedTime(LocalDateTime.now())
                        .build();

        genericDao.create(history);

        System.out.println(
                "Kafka Event Consumed : "
                        + event);
    }
}