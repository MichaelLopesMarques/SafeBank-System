package com.safebank.bank_api.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateAccountRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String owner;

    public CreateAccountRequest(String id, String owner) {
        this.id = id;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }
}
