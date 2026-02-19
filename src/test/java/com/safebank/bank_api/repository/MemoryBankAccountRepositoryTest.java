package com.safebank.bank_api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.safebank.bank_api.domain.BankAccount;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemoryBankAccountRepositoryTest {

    private BankAccount account;
    private BankAccountRepository bankRepo;

    @BeforeEach
    void setUp(){
        account = new BankAccount("AC-DE-2026-01", "Michael Marques");
        bankRepo = new MemoryBankAccountRepository();

        bankRepo.save(account);
    }

    @Test
    public void findById_shouldReturnAccount_whenExists(){
        Optional<BankAccount> result = bankRepo.findById("AC-DE-2026-01");

        assertTrue(result.isPresent());
        assertEquals("AC-DE-2026-01", result.get().getId());
    }

    @Test
    public void findById_shouldReturnEmpty_whenAccountNotExists(){
        Optional<BankAccount> result = bankRepo.findById("AC-DE-2026-02");

        assertTrue(result.isEmpty());
    }

    @Test
    public void save_shouldReturnTrue_whenExists(){
        BankAccount testAccount = new BankAccount("AC-DE-2026-02", "Anton Marques");
        bankRepo.save(testAccount);
        assertTrue(bankRepo.existsById("AC-DE-2026-02"));
    }

    @Test
    public void existsByID_shouldReturnTrue_whenExists(){
        assertTrue(bankRepo.existsById("AC-DE-2026-01"));
    }

    @Test
    public void existsByID_shouldReturnFalse_whenAccountNotExists(){
        assertFalse(bankRepo.existsById("AC-DE-2026-03"));
    }
}