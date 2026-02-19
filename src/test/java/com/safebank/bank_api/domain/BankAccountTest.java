package com.safebank.bank_api.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.safebank.bank_api.exception.AccountLockedException;
import com.safebank.bank_api.exception.InsufficientBalanceException;
import com.safebank.bank_api.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    void setUp(){
        account = new BankAccount("AC-DE-2026-01", "Michael Marques");
    }

    // deposit Tests
    @Test
    public void deposit_shouldIncreaseBalance(){
        account.deposit(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), account.getBalance());
    }

    @Test
    public void deposit_shouldThrowException_whenZero(){
        assertThrows(InvalidAmountException.class, () -> account.deposit(BigDecimal.valueOf(0)));
    }

    @Test
    public void deposit_shouldThrowException_whenNegative(){
        assertThrows(InvalidAmountException.class, () -> account.deposit(BigDecimal.valueOf(-50)));
    }

    @Test
    public void deposit_shouldThrowException_whenAccountLocked(){
        account.lock();
        assertThrows(AccountLockedException.class, () -> account.deposit(BigDecimal.valueOf(50)));
    }

    @Test
    public void deposit_shouldSuccess_whenLockedAccountSwitchToUnlock(){
        account.lock();
        account.unlock();
        account.deposit(BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());
    }

//------------------------------------------------------------------------------------------------------

    // withdraw Tests
    @Test
    public void withdraw_shouldReduceBalance(){
        account.deposit(BigDecimal.valueOf(100));
        account.withdraw(BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());
    }

    @Test
    public void withdraw_shouldThrowException_whenZero(){
        assertThrows(InvalidAmountException.class, () -> account.withdraw(BigDecimal.valueOf(0)));
    }

    @Test
    public void withdraw_shouldThrowException_whenNegative(){
        assertThrows(InvalidAmountException.class, () -> account.withdraw(BigDecimal.valueOf(-50)));
    }

    @Test
    public void withdraw_shouldThrowException_whenMoreThanBalance(){
        account.deposit(BigDecimal.valueOf(100));
        assertThrows(InsufficientBalanceException.class, () -> account.withdraw(BigDecimal.valueOf(200)));
    }

    @Test
    public void withdraw_shouldThrowException_whenLockedAccount(){
        account.lock();
        assertThrows(AccountLockedException.class, () -> account.withdraw(BigDecimal.valueOf(200)));
    }

    @Test
    public void withdraw_shouldSuccess_whenLockedAccountSwitchToUnlock(){
        account.lock();
        account.unlock();
        account.deposit(BigDecimal.valueOf(50));
        account.withdraw(BigDecimal.valueOf(25));
        assertEquals(BigDecimal.valueOf(25), account.getBalance());
    }

//------------------------------------------------------------------------------------------------------

    // isLocked Tests
    @Test
    public void isLocked_ShouldReturnTrue(){
        account.lock();
        assertTrue(account.isLocked());
    }

    @Test
    public void isLocked_ShouldReturnFalse(){
        account.lock();
        account.unlock();
        assertFalse(account.isLocked());
    }

//------------------------------------------------------------------------------------------------------

    // transactionHistory Tests
    @Test
    public void transactionHistory_shouldContainsAllTransactions(){
        account.deposit(BigDecimal.valueOf(100));
        account.withdraw(BigDecimal.valueOf(50));
        account.deposit(BigDecimal.valueOf(80));
        account.withdraw(BigDecimal.valueOf(100));
        List<Transaction> history = account.getTransactionHistory();
        assertEquals(4, history.size());
    }

    @Test
    public void transactionHistory_shouldBeNotNull(){
        List<Transaction> history = account.getTransactionHistory();
        assertNotNull(history);
    }

    @Test
    public void transactionHistory_shouldBeEmpty(){
        List<Transaction> history = account.getTransactionHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    public void transactionHistory_shouldBeRightType(){
        account.deposit(BigDecimal.valueOf(50));
        Transaction typeTest = account.getTransactionHistory().getFirst();
        assertEquals("DEPOSIT", typeTest.getType());
    }

    @Test
    public void transactionHistory_shouldThrowException_whenInvalidChange(){
        List<Transaction> history = account.getTransactionHistory();
        assertThrows(UnsupportedOperationException.class, () ->
                history.add(new Transaction("DEPOSIT", BigDecimal.valueOf(10), BigDecimal.valueOf(10))));
    }



}