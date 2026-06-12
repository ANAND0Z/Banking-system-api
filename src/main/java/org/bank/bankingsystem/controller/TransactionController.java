package org.bank.bankingsystem.controller;


import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.model.Transaction;
import org.bank.bankingsystem.model.TransactionEvent;
import org.bank.bankingsystem.model.TransactionHistory;
import org.bank.bankingsystem.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transferService;

    @PostMapping
    public String transfer(@RequestParam Long fromId,
                           @RequestParam Long toId,
                           @RequestParam BigDecimal amount) {

        return transferService.transfer(fromId, toId, amount);
    }

    @GetMapping("/transaction-details")
    public List<Transaction> transactions(){
      return transferService.getAll();
    }

    @GetMapping("/transaction/{id}")
    public List<Transaction> get(@PathVariable Long id) {
        return transferService.get(id);
    }


    @GetMapping("/transaction-history")
    public List<TransactionHistory> transactionhistory(){
        return transferService.transactionhistory();
    }

}

