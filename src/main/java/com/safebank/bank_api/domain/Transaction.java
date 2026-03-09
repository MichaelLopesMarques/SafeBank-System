package com.safebank.bank_api.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    private String type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;

    protected Transaction(){}

    public Transaction(String type, BigDecimal amount, BigDecimal balanceAfter){
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    public static Transaction deposit(BankAccount account, BigDecimal amount, BigDecimal balance){
        Transaction t = new Transaction("DEPOSIT", amount, balance);
        t.account = account;
        return t;
    }

    public static Transaction withdraw(BankAccount account, BigDecimal amount, BigDecimal balance){
        Transaction t = new Transaction("WITHDRAW", amount, balance);
        t.account = account;
        return t;
    }

    public Long getId() {
        return id;
    }

    public String getType(){
        return type;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public BigDecimal getBalanceAfter(){
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
