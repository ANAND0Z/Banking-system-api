package org.bank.bankingsystem.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotBlank
    private String customerName;

    @PositiveOrZero
    private BigDecimal balance;
}