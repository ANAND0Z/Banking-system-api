package org.bank.bankingsystem.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.dto.AccountRequest;
import org.bank.bankingsystem.model.Account;
import org.bank.bankingsystem.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Account createAccount(
            @Valid @RequestBody AccountRequest request) {

        return accountService.createAccount(request);
    }

    @GetMapping("/{id}")
    public Account getAccount(
            @PathVariable Long id) {

        return accountService.getAccount(id);
    }

    @GetMapping
    public List<Account> getAllAccounts() {

        return accountService.getAllAccounts();
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(
            @PathVariable Long id) {

        accountService.deleteAccount(id);

        return "Account deleted successfully";
    }
}