package org.bank.bankingsystem.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BeneficiaryRequest {

    @NotBlank
    private String beneficiaryName;

    @NotBlank
    private String beneficiaryAccountNumber;

    @NotBlank
    private String bankName;

    @NotBlank
    private String accountNumber;
}