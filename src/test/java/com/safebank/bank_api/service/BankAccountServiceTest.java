package com.safebank.bank_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.safebank.bank_api.domain.BankAccount;
import com.safebank.bank_api.exception.AccountNotFoundException;
import com.safebank.bank_api.exception.InsufficientBalanceException;
import com.safebank.bank_api.exception.InvalidAmountException;
import com.safebank.bank_api.repository.MemoryBankAccountRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {

    private BankAccount account;
    private MemoryBankAccountRepository bankRepo;
    private BankAccountService bankService;

    @BeforeEach
    void setUp(){
        account = new BankAccount("AC-DE-2026-01", "Michael Marques");
        bankRepo = new MemoryBankAccountRepository();
        bankService = new BankAccountService(bankRepo);

        bankRepo.save(account);
    }

    @Test
    public void createAccount_shouldSuccess(){
        bankService.createAccount("AC-DE-2026-02", "Michael Marques");
        assertTrue(bankRepo.existsById("AC-DE-2026-02"));
    }

    @Test
    public void createAccount_shouldThrowException_whenAccountExists(){
        assertThrows(IllegalStateException.class, () -> bankService.createAccount("AC-DE-2026-01", "Michael Marques"));
    }

    @Test
    public void deposit_shouldReturnRightBalance(){
        bankService.deposit("AC-DE-2026-01", BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), bankService.getBalance("AC-DE-2026-01"));
    }

    @Test
    public void deposit_shouldThrowException_whenNegativeAmount(){
        assertThrows(InvalidAmountException.class,
                () -> bankService.deposit("AC-DE-2026-01", BigDecimal.valueOf(-100)));
    }

    @Test
    public void deposit_shouldThrowException_whenAccountNotExisting(){
        assertThrows(AccountNotFoundException.class,
                () -> bankService.deposit("AC-DE-2026-03", BigDecimal.valueOf(100)));
    }

    @Test
    public void withdraw_shouldReturnRightBalance(){
        bankService.deposit("AC-DE-2026-01", BigDecimal.valueOf(100));
        bankService.withdraw("AC-DE-2026-01", BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), bankService.getBalance("AC-DE-2026-01"));
    }

    @Test
    public void withdraw_shouldThrowException_whenNegativeAmount(){
        assertThrows(InvalidAmountException.class,
                () -> bankService.withdraw("AC-DE-2026-01", BigDecimal.valueOf(-100)));
    }

    @Test
    public void withdraw_shouldThrowException_whenInsufficientBalance(){
        assertThrows(InsufficientBalanceException.class,
                () -> bankService.withdraw("AC-DE-2026-01", BigDecimal.valueOf(100)));
    }

    @Test
    public void withdraw_shouldThrowException_whenAccountNotExisting(){
        assertThrows(AccountNotFoundException.class,
                () -> bankService.withdraw("AC-DE-2026-03", BigDecimal.valueOf(100)));
    }

    @Test
    public void getBalance_shouldSuccess(){
        assertEquals(BigDecimal.valueOf(0), bankService.getBalance("AC-DE-2026-01"));
    }

    @Test
    public void getBalance_shouldThrowException_whenAccountNotFound(){
        assertThrows(AccountNotFoundException.class,
                () -> bankService.getBalance("AC-DE-2026-03"));
    }

    @Test
    public void lockAccount_shouldSucess(){
        bankService.lockAccount("AC-DE-2026-01");
        assertTrue(account.isLocked());
    }

    @Test
    public void lockAccount_shouldThrowException_whenAccountNotFound(){
        assertThrows(AccountNotFoundException.class,
                () -> bankService.lockAccount("AC-DE-2026-03"));
    }

    @Test
    public void unlockAccount_shouldSucess(){
        bankService.lockAccount("AC-DE-2026-01");
        bankService.unlockAccount("AC-DE-2026-01");
        assertFalse(account.isLocked());
    }

    @Test
    public void unlockAccount_shouldThrowException_whenAccountNotFound(){
        assertThrows(AccountNotFoundException.class,
                () -> bankService.unlockAccount("AC-DE-2026-03"));
    }

    @Test
    public void getAccount_shouldSuccess(){
        assertEquals(account, bankService.getAccount("AC-DE-2026-01"));
    }

    @Test
    public void getAccount_shouldThrowException_whenAccountNotFound(){
        assertThrows(AccountNotFoundException.class,
                () -> bankService.getAccount("AC-DE-2026-03"));
    }
}