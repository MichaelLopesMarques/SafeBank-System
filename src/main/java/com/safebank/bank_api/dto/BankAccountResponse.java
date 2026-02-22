package com.safebank.bank_api.dto;

import java.math.BigDecimal;

public class BankAccountResponse {

    private final String id;
    private final String owner;
    private final BigDecimal balance;
    private final boolean locked;

    public BankAccountResponse(String id, String owner, BigDecimal balance, boolean locked){
        this.id = id;
        this.owner = owner;
        this.balance = balance;
        this.locked = locked;
    }

    public String getId(){
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isLocked() {
        return locked;
    }
}
