package com.safebank.bank_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safebank.bank_api.dto.CreateAccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class BankAccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final AtomicInteger accountCounter = new AtomicInteger(1);

    private String createAccount() throws Exception {

        int year = Year.now().getValue();

        String id = String.format("AC-DE-%d-%02d", year, accountCounter.getAndIncrement());

        String json = """
        {
          "id": "%s",
          "owner": "Peter Parker"
        }
        """.formatted(id);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        return id;
    }

    @Test
    void shouldCreateAccountSuccessfully() throws Exception{
        createAccount();

        mockMvc.perform(get("/accounts/AC-DE-2026-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("AC-DE-2026-01"))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(false));
    }

    @Test
    void shouldCreateAccountThrowException() throws Exception{
        createAccount();

        String json = """
        {
          "id": "AC-DE-2026-01",
          "owner": "Peter Parker"
        }
        """;

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldDepositSuccessfully() throws Exception{
        String id = createAccount();

        String depositJson = """
        {
          "amount": 100
        }
        """;

        mockMvc.perform(post("/accounts/" + id + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    void shouldDepositThrowException() throws Exception{
        String id = createAccount();

        String depositJson = """
        {
          "amount": -50
        }
        """;

        mockMvc.perform(post("/accounts/" + id + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isBadRequest() );
    }

    @Test
    void shouldWithdrawSuccessfully() throws Exception{
        String id = createAccount();

        String depositJson = """
        {
          "amount": 100
        }
        """;

        mockMvc.perform(post("/accounts/" + id + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isOk());

        String withdrawJson = """
        {
          "amount": 100
        }
        """;

        mockMvc.perform(post("/accounts/" + id + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(false));
    }

    @Test
    void shouldWithdrawThrowException() throws Exception{
        String id = createAccount();

        String depositJson = """
        {
          "amount": 100
        }
        """;

        mockMvc.perform(post("/accounts/" + id + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isOk());

        String withdrawJson = """
        {
          "amount": 200
        }
        """;

        mockMvc.perform(post("/accounts/" + id + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBalanceSuccessfully() throws Exception{
        String id = createAccount();

        mockMvc.perform(get("/accounts/" + id + "/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.00"));
    }

    @Test
    void shouldLockSuccessfully() throws Exception{
        String id = createAccount();

        mockMvc.perform(post("/accounts/" + id + "/lock"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(true));
    }

    @Test
    void shouldNotDepositWhenLockedThrowException() throws Exception{
        String id = createAccount();

        mockMvc.perform(post("/accounts/" + id + "/lock"))
                .andExpect(status().isNoContent());

        String depositJson = """
        {
          "amount": 100
        }
        """;

        mockMvc.perform(post("/accounts/" + id + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isLocked());
    }

    @Test
    void shouldLockUnlockSuccessfully() throws Exception{
        String id = createAccount();

        mockMvc.perform(post("/accounts/" + id + "/lock"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts/"+ id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(true));

        mockMvc.perform(post("/accounts/" + id + "/unlock"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts/"+ id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.owner").value("Peter Parker"))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.locked").value(false));
    }

}