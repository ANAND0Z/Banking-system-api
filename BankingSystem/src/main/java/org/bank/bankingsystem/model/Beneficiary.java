package org.bank.bankingsystem.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "beneficiaries",
        indexes = {
                @Index(
                        name = "idx_beneficiary_account",
                        columnList = "beneficiaryAccountNumber"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String beneficiaryName;

    @Column(nullable = false)
    private String beneficiaryAccountNumber;

    private String bankName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}