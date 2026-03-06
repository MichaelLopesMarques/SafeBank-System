package com.safebank.bank_api.service;

import com.safebank.bank_api.domain.BankAccount;
import com.safebank.bank_api.domain.Transaction;
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
    private final TransactionRepository transactionRepository;

    public BankAccountService(BankAccountRepository repository, TransactionRepository transactionRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
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
        transactionRepository.save(Transaction.deposit(account, amount, account.getBalance()));

        return repository.save(account);
    }

    public BankAccount withdraw(String accountId, BigDecimal amount){
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        account.withdraw(amount);
        transactionRepository.save(Transaction.withdraw(account, amount, account.getBalance()));

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

    public List<Transaction> getTransactions(String accountId){
        if(!repository.existsById(accountId)){
            throw new AccountNotFoundException("Account not found: " + accountId);
        }
        return transactionRepository.findByAccountId(accountId);
    }

}
