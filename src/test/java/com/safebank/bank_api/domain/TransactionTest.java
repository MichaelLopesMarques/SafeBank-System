package com.safebank.bank_api.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {

    // Transaction deposit() Tests
    @Test
    public void deposit_Transaction_shouldSuccess(){
        Transaction transaction = Transaction.deposit(BigDecimal.valueOf(50), BigDecimal.valueOf(450));
        assertAll("Transaction fields",
                () -> assertEquals("DEPOSIT", transaction.getType()),
                () -> assertEquals(BigDecimal.valueOf(50), transaction.getAmount()),
                () -> assertEquals(BigDecimal.valueOf(450), transaction.getBalanceAfter())
        );
    }

    @Test
    public void deposit_shouldTrackTransactionhistory(){
        BankAccount account = new BankAccount("AC-DE-2026-01", "Peter Parker");

        account.deposit(BigDecimal.valueOf(100));
        List<Transaction> history = account.getTransactionHistory();

        assertEquals(1, history.size());
        Transaction lastTransaction = history.getFirst();

        assertAll("Transaction fields",
                () -> assertEquals("DEPOSIT", lastTransaction.getType()),
                () -> assertEquals(BigDecimal.valueOf(100), lastTransaction.getAmount()),
                () -> assertEquals(BigDecimal.valueOf(100), lastTransaction.getBalanceAfter())
        );

    }

//------------------------------------------------------------------------------------------------------

    // Transaction withdraw() Tests
    @Test
    public void withdraw_Transaction_shouldSuccess(){
        Transaction transaction = Transaction.withdraw(BigDecimal.valueOf(50), BigDecimal.valueOf(450));
        assertAll("Transaction fields",
                () -> assertEquals("WITHDRAW", transaction.getType()),
                () -> assertEquals(BigDecimal.valueOf(50), transaction.getAmount()),
                () -> assertEquals(BigDecimal.valueOf(450), transaction.getBalanceAfter())
        );
    }

    @Test
    public void withdraw_shouldTrackTransactionhistory(){
        BankAccount account = new BankAccount("AC-DE-2026-01", "Peter Parker");

        account.deposit(BigDecimal.valueOf(200));
        account.withdraw(BigDecimal.valueOf(100));
        List<Transaction> history = account.getTransactionHistory();

        assertEquals(2, history.size());
        Transaction lastTransaction = history.get(1);

        assertAll("Transaction fields",
                () -> assertEquals("WITHDRAW", lastTransaction.getType()),
                () -> assertEquals(BigDecimal.valueOf(100), lastTransaction.getAmount()),
                () -> assertEquals(BigDecimal.valueOf(100), lastTransaction.getBalanceAfter())
        );

    }
}