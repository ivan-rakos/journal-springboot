package com.example.trading.trading.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trading.trading.dto.AccountDTO;
import com.example.trading.trading.dto.CreateAccountDTO;
import com.example.trading.trading.dto.TradeSummaryDTO;
import com.example.trading.trading.dto.UpdateAccountDTO;
import com.example.trading.trading.mappers.AccountMapper;
import com.example.trading.trading.mappers.TradeMapper;
import com.example.trading.trading.models.Account;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.services.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        List<Account> accounts = service.getAllAccounts();
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(AccountMapper.accountToDTOList(accounts));

    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long accountId) {
        Account account = service.getById(accountId).get();
        return ResponseEntity.status(HttpStatus.OK).body(AccountMapper.accountToDTO(account));
    }

    @GetMapping("/{accountId}/trades")
    public ResponseEntity<List<TradeSummaryDTO>> getTradesByAccountId(@PathVariable Long accountId) {
        List<Trade> trades = service.getTradesByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(TradeMapper.tradeToSummaryDTOList(trades));
    }

    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody CreateAccountDTO account) {
        Account created = service.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long accountId,
            @Valid @RequestBody UpdateAccountDTO updatedAccount) {
        Account updated = service.updateAccount(accountId, updatedAccount);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> delete(@PathVariable Long accountId) {
        service.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

}
