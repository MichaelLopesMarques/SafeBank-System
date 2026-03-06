package com.safebank.bank_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private LocalDateTime timestamp;

    public TransactionResponse(Long id, String type, BigDecimal amount, BigDecimal balanceAfter, LocalDateTime timestamp){
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
