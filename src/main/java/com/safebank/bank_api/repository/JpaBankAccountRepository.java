package com.safebank.bank_api.repository;

import com.safebank.bank_api.domain.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaBankAccountRepository implements BankAccountRepository{

    private final SpringDataBankAccountRepository repo;

    public JpaBankAccountRepository(SpringDataBankAccountRepository repo){
        this.repo = repo;
    }

    @Override
    public Optional<BankAccount> findById(String accountId) {
        return repo.findById(accountId);
    }

    @Override
    public BankAccount save(BankAccount account) {
        return repo.save(account);
    }

    @Override
    public boolean existsById(String accountId) {
        return repo.existsById(accountId);
    }
}
