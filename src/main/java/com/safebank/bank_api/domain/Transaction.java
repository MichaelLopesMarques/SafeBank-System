package com.safebank.bank_api.domain;

import java.math.BigDecimal;

public class Transaction {

    private final String type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;

    public Transaction(String type, BigDecimal amount, BigDecimal balanceAfter){
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public static Transaction deposit(BigDecimal amount, BigDecimal balance){
        return new Transaction("DEPOSIT", amount, balance);
    }

    public static Transaction withdraw(BigDecimal amount, BigDecimal balance){
        return new Transaction("WITHDRAW", amount, balance);
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
}
