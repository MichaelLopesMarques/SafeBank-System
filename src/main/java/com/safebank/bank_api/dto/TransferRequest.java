package com.safebank.bank_api.dto;

import java.math.BigDecimal;

public class TransferRequest {

    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
