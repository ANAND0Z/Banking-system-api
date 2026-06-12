package org.bank.bankingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TransactionEvent")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transactionId;

    private String fromAccount;

    private String toAccount;

    private BigDecimal amount;

    private LocalDateTime eventTime;
}
