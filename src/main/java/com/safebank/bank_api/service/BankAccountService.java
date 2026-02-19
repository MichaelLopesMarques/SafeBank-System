package com.safebank.bank_api.service;

import com.safebank.bank_api.domain.BankAccount;
import com.safebank.bank_api.exception.AccountNotFoundException;
import com.safebank.bank_api.repository.BankAccountRepository;

import java.math.BigDecimal;

public class BankAccountService {

    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    public void createAccount(String accountId, String name){
        if (repository.existsById(accountId)){
            throw new IllegalStateException("Account already exists!");
        }
        repository.save(new BankAccount(accountId, name));
    }

    public void deposit(String accountId, BigDecimal amount){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
        account.deposit(amount);
        repository.save(account);
    }

    public void withdraw(String accountId, BigDecimal amount){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
        account.withdraw(amount);
        repository.save(account);
    }

    public BigDecimal getBalance(String accountId){
        return repository.findById(accountId)
                .map(BankAccount::getBalance)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

    public void lockAccount(String accountId){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
        account.lock();
        repository.save(account);
    }

    public void unlockAccount(String accountId){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
        account.unlock();
        repository.save(account);
    }

    public BankAccount getAccount(String accountId){
        return repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

}
