package com.safebank.bank_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safebank.bank_api.dto.CreateAccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                        .content(objectMapper.writeValueAsString(duplicate)))
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

    @Test
    void shouldDepositThrowException() throws Exception{
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
          "amount": -50
        }
        """;

        mockMvc.perform(post("/accounts/AC-DE-2026-01/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isBadRequest() );
    }

    @Test
    void shouldWithdrawSuccessfully() throws Exception{
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
                .andExpect(status().isOk());

        String withdrawJson = """
        {
          "amount": 100
        }
        """;

        mockMvc.perform(post("/accounts/AC-DE-2026-01/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("AC-DE-2026-01"))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(false));
    }

    @Test
    void shouldWithdrawThrowException() throws Exception{
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
                .andExpect(status().isOk());

        String withdrawJson = """
        {
          "amount": 200
        }
        """;

        mockMvc.perform(post("/accounts/AC-DE-2026-01/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBalanceSuccessfully() throws Exception{
        CreateAccountRequest request = new CreateAccountRequest("AC-DE-2026-01", "Peter Parker");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/accounts/AC-DE-2026-01/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.00"));
    }

    @Test
    void shouldLockSuccessfully() throws Exception{
        CreateAccountRequest request = new CreateAccountRequest("AC-DE-2026-01", "Peter Parker");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts/AC-DE-2026-01/lock"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts/AC-DE-2026-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("AC-DE-2026-01"))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(true));
    }

    @Test
    void shouldNotDepositWhenLockedThrowException() throws Exception{
        CreateAccountRequest request = new CreateAccountRequest("AC-DE-2026-01", "Peter Parker");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts/AC-DE-2026-01/lock"))
                .andExpect(status().isNoContent());

        String depositJson = """
        {
          "amount": 100
        }
        """;

        mockMvc.perform(post("/accounts/AC-DE-2026-01/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isLocked());
    }

    @Test
    void shouldLockUnlockSuccessfully() throws Exception{
        CreateAccountRequest request = new CreateAccountRequest("AC-DE-2026-01", "Peter Parker");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts/AC-DE-2026-01/lock"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts/AC-DE-2026-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("AC-DE-2026-01"))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(true));

        mockMvc.perform(post("/accounts/AC-DE-2026-01/unlock"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts/AC-DE-2026-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("AC-DE-2026-01"))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(false));
    }

}