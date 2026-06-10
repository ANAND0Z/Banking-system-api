package org.bank.bankingsystem.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.dto.TransferRequest;
import org.bank.bankingsystem.model.Transaction;
import org.bank.bankingsystem.service.TransactionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public Transaction transfer(
            @Valid
            @RequestBody TransferRequest request) {

        return transactionService.transfer(request);
    }
}