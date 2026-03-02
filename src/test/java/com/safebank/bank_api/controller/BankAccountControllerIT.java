package com.safebank.bank_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safebank.bank_api.dto.CreateAccountRequest;
import com.safebank.bank_api.dto.DepositRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BankAccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAccountSuccessfully() throws Exception{
        CreateAccountRequest request = new CreateAccountRequest("AC-DE-2026-01", "Peter Parker");

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("AC-DE-2026-01"))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(false));
    }

    @Test
    void shouldCreateAccountThrowException() throws Exception{
        CreateAccountRequest request = new CreateAccountRequest("AC-DE-2026-01", "Peter Parker");
        CreateAccountRequest duplicate = new CreateAccountRequest("AC-DE-2026-01", "Max Milo");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldDepositSuccessfully() throws Exception{
        String createJson = """
        {
          "id": "AC-DE-2026-01",
          "owner": "Peter Parker"
        }
        """;

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                        .andExpect(status().isCreated());

        String depositJson = """
        {
          "amount": 100
        }
        """;

        mockMvc.perform(post("/accounts/AC-DE-2026-01/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("AC-DE-2026-01"))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.locked").value(false));
    }

}