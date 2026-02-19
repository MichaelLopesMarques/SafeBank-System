package com.safebank.bank_api.repository;

import com.safebank.bank_api.domain.BankAccount;

import java.util.Optional;


public interface BankAccountRepository {

    Optional<BankAccount> findById(String accountId);

    BankAccount save(BankAccount account);

    boolean existsById(String accountId);
}
