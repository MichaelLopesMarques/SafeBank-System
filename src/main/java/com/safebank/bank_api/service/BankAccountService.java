package com.safebank.bank_api.service;

import com.safebank.bank_api.domain.BankAccount;
import com.safebank.bank_api.exception.AccountNotFoundException;
import com.safebank.bank_api.exception.InsufficientBalanceException;
import com.safebank.bank_api.exception.InvalidAmountException;
import com.safebank.bank_api.repository.BankAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    public BankAccount createAccount(String accountId, String name){
        if (repository.existsById(accountId)){
            throw new IllegalStateException("Account already exists!");
        }
        return repository.save(new BankAccount(accountId, name));
    }

    public BankAccount deposit(String accountId, BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }

        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        account.deposit(amount);
        return account;
    }

    public BankAccount withdraw(String accountId, BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }

        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        if (account.getBalance().compareTo(amount) < 0){
            throw new InsufficientBalanceException("Balance not enough");
        }

        account.withdraw(amount);
        return account;
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
    }

    public void unlockAccount(String accountId){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
        account.unlock();
    }

    public BankAccount getAccount(String accountId){
        return repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

}
