package com.safebank.bank_api.service;

import com.safebank.bank_api.domain.BankAccount;
import com.safebank.bank_api.domain.Transaction;
import com.safebank.bank_api.dto.TransactionResponse;
import com.safebank.bank_api.exception.AccountAlreadyExistsException;
import com.safebank.bank_api.exception.AccountNotFoundException;
import com.safebank.bank_api.repository.BankAccountRepository;
import com.safebank.bank_api.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository, TransactionRepository transactionRepository) {
        this.repository = repository;
    }

    public BankAccount createAccount(String accountId, String name){
        if (repository.existsById(accountId)){
            throw new AccountAlreadyExistsException("Account already exists: " + accountId);
        }
        return repository.save(new BankAccount(accountId, name));
    }

    public BankAccount deposit(String accountId, BigDecimal amount){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        account.deposit(amount);
        return repository.save(account);
    }

    public BankAccount withdraw(String accountId, BigDecimal amount){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        account.withdraw(amount);
        return repository.save(account);
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
        System.out.println("BEFORE" + account.isLocked());
        account.unlock();
        System.out.println("AFTER" + account.isLocked());
        repository.save(account);
    }

    public BankAccount getAccount(String accountId){
        return repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

    public List<TransactionResponse> getTransactions(String accountId){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        return account.getTransactionHistory()
                .stream()
                .map(t -> new TransactionResponse(
                        t.getId(),
                        t.getType(),
                        t.getAmount(),
                        t.getBalanceAfter(),
                        t.getTimestamp()
                )).toList();
    }

}
