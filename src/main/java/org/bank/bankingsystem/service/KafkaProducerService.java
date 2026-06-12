package org.bank.bankingsystem.service;

import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.model.TransactionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public void publish(TransactionEvent event) {

        kafkaTemplate.send(
                "bank-transactions",
                event
        );
    }
}

