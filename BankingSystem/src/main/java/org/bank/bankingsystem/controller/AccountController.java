package org.bank.bankingsystem.controller;


import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.model.Account;
import org.bank.bankingsystem.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public Account create(@RequestBody Account account) {
        return accountService.save(account);
    }

    @PostMapping("/update")
    public Account update(@RequestBody Account account) {
        return accountService.update(account);
    }


    @GetMapping("/{id}")
    public Account get(@PathVariable Long id) {
        return accountService.get(id);
    }

    @GetMapping("/getall")
    public List<Account> getAll() {
        return accountService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        accountService.delete(id);
    }
}


