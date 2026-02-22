package com.safebank.bank_api.controller;

import com.safebank.bank_api.domain.BankAccount;
import com.safebank.bank_api.dto.BankAccountResponse;
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
    public BankAccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        BankAccount account = service.createAccount(request.id(), request.owner());
        return mapToResponse(account);
    }

    @PostMapping("/{id}/deposit")
    public BankAccountResponse deposit(
            @PathVariable String id,
            @RequestBody AmountRequest request) {

        return mapToResponse(service.deposit(id, request.amount()));
    }

    @PostMapping("/{id}/withdraw")
    public BankAccountResponse withdraw(
            @PathVariable String id,
            @RequestBody AmountRequest request) {

        return mapToResponse(service.withdraw(id, request.amount()));
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable String id) {
        return service.getBalance(id);
    }

    @GetMapping("/{id}")
    public BankAccountResponse getAccount(@PathVariable String id) {
        return mapToResponse(service.getAccount(id));
    }

    private BankAccountResponse mapToResponse(BankAccount account) {
        return new BankAccountResponse(
                account.getId(),
                account.getOwner(),
                account.getBalance(),
                account.isLocked()
        );
    }

}
