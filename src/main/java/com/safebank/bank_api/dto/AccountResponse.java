package com.safebank.bank_api.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private String id;
    private String owner;
    private BigDecimal balance;
    private boolean lockStatus;

    public AccountResponse(String id, String owner, BigDecimal balance, boolean lockStatus){
        this.id = id;
        this.owner = owner;
        this.balance = balance;
        this.lockStatus = lockStatus;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isLockStatus() {
        return lockStatus;
    }
}
