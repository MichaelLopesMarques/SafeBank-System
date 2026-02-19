package com.safebank.bank_api.repository;

import com.safebank.bank_api.domain.BankAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryBankAccountRepository implements BankAccountRepository {

    private final Map<String, BankAccount> storage = new HashMap<>();

    @Override
    public Optional<BankAccount> findById(String accountId) {
        return Optional.ofNullable(storage.get(accountId));
    }

    @Override
    public void save(BankAccount account) {
        storage.put(account.getId(), account);
    }

    @Override
    public boolean existsById(String accountId) {
        return storage.containsKey(accountId);
    }
}
