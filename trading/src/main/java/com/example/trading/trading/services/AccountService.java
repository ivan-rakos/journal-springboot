package com.example.trading.trading.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.trading.trading.dto.CreateAccountDTO;
import com.example.trading.trading.dto.UpdateAccountDTO;
import com.example.trading.trading.models.Account;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.repositories.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    public Account createAccount(CreateAccountDTO dto) {
        Account account = new Account(dto.getName(), dto.getBalance());
        return repo.save(account);
    }

    public Optional<Account> getById(Long id) {
        return repo.findById(id);
    }

    public List<Account> getAllAccounts() {
        return repo.findAll();
    }

    public void deleteAccount(Long id) {
        if (!repo.existsById(id))
            throw new RuntimeException("Account not found");
        repo.deleteById(id);
    }

    public Account updateAccount(Long id, UpdateAccountDTO updatedAccount) {
        Account account = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (updatedAccount.getName() != null)
            account.setName(updatedAccount.getName());
        if (updatedAccount.getBalance() != null)
            account.setBalance(updatedAccount.getBalance());
        if (updatedAccount.getActive() != account.isActive())
            account.setActive(updatedAccount.getActive());
        return repo.save(account);
    }

    public List<Trade> getTradesByAccountId(Long id) {
        Account account = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found " + id));
        Set<Trade> trades = account.getTrades();
        return new ArrayList<>(trades);
    }

}
