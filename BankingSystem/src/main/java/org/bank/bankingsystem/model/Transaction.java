package org.bank.bankingsystem.model;




import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(
                        name = "idx_transaction_status",
                        columnList = "transactionStatus"
                ),
                @Index(
                        name = "idx_transaction_time",
                        columnList = "transactionTime"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sourceAccountId;

    private Long targetAccountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private LocalDateTime transactionTime;

    @PrePersist
    public void onCreate() {
        transactionTime = LocalDateTime.now();
    }
}