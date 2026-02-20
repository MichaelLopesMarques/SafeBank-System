package com.safebank.bank_api.controller;

import com.safebank.bank_api.domain.BankAccount;
import com.safebank.bank_api.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService service){
        this.service = service;
    }

    public record CreateAccountRequest(String id, String owner) {}

    public record AmountRequest(BigDecimal amount) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccount createAccount(@RequestBody CreateAccountRequest request) {
        return service.createAccount(request.id(), request.owner());
    }

    @PostMapping("/{id}/deposit")
    public void deposit(
            @PathVariable String id,
            @RequestBody AmountRequest request) {

        service.deposit(id, request.amount());
    }

    @PostMapping("/{id}/withdraw")
    public void withdraw(
            @PathVariable String id,
            @RequestBody AmountRequest request) {

        service.withdraw(id, request.amount());
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable String id) {
        return service.getBalance(id);
    }

    @GetMapping("/{id}")
    public BankAccount getAccount(@PathVariable String id) {
        return service.getAccount(id);
    }


}
