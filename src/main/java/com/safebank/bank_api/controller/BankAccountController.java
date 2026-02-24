package com.safebank.bank_api.controller;

import com.safebank.bank_api.domain.BankAccount;
import com.safebank.bank_api.dto.BankAccountResponse;
import com.safebank.bank_api.dto.CreateAccountRequest;
import com.safebank.bank_api.dto.DepositRequest;
import com.safebank.bank_api.dto.WithdrawRequest;
import com.safebank.bank_api.service.BankAccountService;
import jakarta.validation.Valid;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        BankAccount account = service.createAccount(request.getId(), request.getOwner());
        return mapToResponse(account);
    }

    @PostMapping("/{id}/deposit")
    public BankAccountResponse deposit(
            @PathVariable String id,
            @Valid @RequestBody DepositRequest request) {

        return mapToResponse(service.deposit(id, request.getAmount()));
    }

    @PostMapping("/{id}/withdraw")
    public BankAccountResponse withdraw(
            @PathVariable String id,
            @Valid @RequestBody WithdrawRequest request) {

        return mapToResponse(service.withdraw(id, request.getAmount()));
    }

    @PostMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void lock(@PathVariable String id){
        service.lockAccount(id);
    }

    @PostMapping("/{id}/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlock(@PathVariable String id){
        service.unlockAccount(id);
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
