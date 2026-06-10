package org.bank.bankingsystem.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.dto.BeneficiaryRequest;
import org.bank.bankingsystem.model.Beneficiary;
import org.bank.bankingsystem.service.BeneficiaryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    public Beneficiary addBeneficiary(
            @Valid
            @RequestBody BeneficiaryRequest request) {

        return beneficiaryService
                .addBeneficiary(request);
    }
}