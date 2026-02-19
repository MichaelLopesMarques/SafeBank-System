package com.safebank.bank_api.repository;

import com.safebank.bank_api.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBankAccountRepository extends JpaRepository<BankAccount, String> {
}
