package com.example.trading.trading.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.trading.trading.TestDataFactory;
import com.example.trading.trading.dto.CreateAccountDTO;
import com.example.trading.trading.dto.UpdateAccountDTO;
import com.example.trading.trading.exceptions.BusinessRuleException;
import com.example.trading.trading.exceptions.ResourceNotFoundException;
import com.example.trading.trading.models.Account;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AccountService accountService;

        @Autowired
        private ObjectMapper objectMapper;

        // Get account tests

        @Test
        void getAllAccountsSuccess() throws Exception {
                when(accountService.getAllAccounts()).thenReturn(TestDataFactory.getAccountList());

                mockMvc.perform(get("/api/accounts"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2));

                verify(accountService).getAllAccounts();
        }

        @Test
        void getAllAccountsNoContent() throws Exception {
                when(accountService.getAllAccounts()).thenReturn(java.util.Collections.emptyList());

                mockMvc.perform(get("/api/accounts"))
                                .andExpect(status().isNoContent());

                verify(accountService).getAllAccounts();
        }

        @Test
        void getAccountByIdSuccess() throws Exception {
                Account account = TestDataFactory.createValidAccount(1L, "Test Account");
                when(accountService.getById(1L)).thenReturn(java.util.Optional.of(account));

                mockMvc.perform(get("/api/accounts/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.name").value("Test Account"))
                                .andExpect(jsonPath("$.balance").value(1000.00));

                verify(accountService).getById(1L);
        }

        @Test
        void getTradesByAccountIdSuccess() throws Exception {
                Set<Trade> trades = TestDataFactory.getAccountList().get(0).getTrades();
                when(accountService.getTradesByAccountId(1L)).thenReturn(trades.stream().toList());

                mockMvc.perform(get("/api/accounts/1/trades"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(trades.size()));

                verify(accountService).getTradesByAccountId(1L);
        }

        @Test
        void getTradesByAccountIdNoTrades() throws Exception {
                when(accountService.getTradesByAccountId(1L)).thenReturn(java.util.Collections.emptyList());
                mockMvc.perform(get("/api/accounts/1/trades"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(0));

                verify(accountService).getTradesByAccountId(1L);
        }

        @Test
        void getTradesByAccountIdNotFound() throws Exception {
                when(accountService.getTradesByAccountId(1L))
                                .thenThrow(new ResourceNotFoundException("Account with id 1 not found"));

                mockMvc.perform(get("/api/accounts/1/trades"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Account with id 1 not found"));

                verify(accountService).getTradesByAccountId(1L);
        }

        @Test
        void getAccountByIdNotFound() throws Exception {
                when(accountService.getById(1L))
                                .thenThrow(new ResourceNotFoundException("Account with id 1 not found"));

                mockMvc.perform(get("/api/accounts/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Account with id 1 not found"));

                verify(accountService).getById(1L);
        }

        // Post account tests

        @Test
        void createAccountSuccess() throws Exception {
                CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();
                Account account = TestDataFactory.createValidAccount(1L, "Test Account");
                when(accountService.createAccount(any(CreateAccountDTO.class))).thenReturn(account);

                mockMvc.perform(post("/api/accounts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(createAccountDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.name").value("Test Account"))
                                .andExpect(jsonPath("$.balance").value(1000.00));

                verify(accountService).createAccount(any(CreateAccountDTO.class));
        }

        @Test
        void createAccountInvalidData() throws Exception {

                CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();
                createAccountDTO.setBalance(-100.00); // Invalid balance
                mockMvc.perform(post("/api/accounts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(createAccountDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error.balance").value("Balance must be non-negative"));
        }

        @Test
        void createAccountMissingName() throws Exception {

                CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();
                createAccountDTO.setName(null); // Missing name
                mockMvc.perform(post("/api/accounts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(createAccountDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error.name").value("Name is required"));
        }

        @Test
        void createAccountWithExistingName() throws Exception {
                CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();
                when(accountService.createAccount(any(CreateAccountDTO.class)))
                                .thenThrow(new BusinessRuleException("Account name already exists"));

                mockMvc.perform(post("/api/accounts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(createAccountDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Account name already exists"));

                verify(accountService).createAccount(any(CreateAccountDTO.class));
        }

        // Patch account tests

        @Test
        void updateAccountSuccess() throws Exception {
                UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();
                Account updatedAccount = TestDataFactory.updatedAccount();

                when(accountService.updateAccount(1L, updateAccountDTO)).thenReturn(updatedAccount);

                mockMvc.perform(patch("/api/accounts/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateAccountDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.name").value("Updated Account"))
                                .andExpect(jsonPath("$.balance").value(2000.00))
                                .andExpect(jsonPath("$.active").value(true));

                verify(accountService).updateAccount(1L, updateAccountDTO);
        }

        @Test
        void updateAccountNullFields() throws Exception {
                UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();
                updateAccountDTO.setName(null);
                updateAccountDTO.setActive(false);

                Account existingAccount = TestDataFactory.createValidAccount(1L, "Test Account");
                existingAccount.setActive(false); // only active field changes

                when(accountService.updateAccount(1L, updateAccountDTO)).thenReturn(existingAccount);

                mockMvc.perform(patch("/api/accounts/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateAccountDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.name").value("Test Account"))
                                .andExpect(jsonPath("$.balance").value(1000.00))
                                .andExpect(jsonPath("$.active").value(false));

                verify(accountService).updateAccount(1L, updateAccountDTO);
        }

        @Test
        void updateAccountEmptyName() throws Exception {
                UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();
                updateAccountDTO.setName(""); // Invalid balance

                mockMvc.perform(patch("/api/accounts/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateAccountDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error.name").exists());
        }

        @Test
        void updateAccountBadName() throws Exception {
                UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();
                updateAccountDTO.setName("ac"); // Invalid balance

                mockMvc.perform(patch("/api/accounts/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateAccountDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error.name").value("Name must be between 3 and 50 characters"));
        }

        @Test
        void updateAccountNotFound() throws Exception {
                UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();

                when(accountService.updateAccount(1L, updateAccountDTO))
                                .thenThrow(new ResourceNotFoundException("Account with id 1 not found"));

                mockMvc.perform(patch("/api/accounts/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateAccountDTO)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Account with id 1 not found"));

                verify(accountService).updateAccount(1L, updateAccountDTO);
        }

        // Delete account tests

        @Test
        void deleteAccountSuccess() throws Exception {
                mockMvc.perform(delete("/api/accounts/1"))
                                .andExpect(status().isNoContent());

                verify(accountService).deleteAccount(1L);
        }

        @Test
        void deleteAccountNotFound() throws Exception {
                doThrow(new ResourceNotFoundException("Account with id 1 not found")).when(accountService)
                                .deleteAccount(1L);

                mockMvc.perform(delete("/api/accounts/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Account with id 1 not found"));

                verify(accountService).deleteAccount(1L);
        }
}
