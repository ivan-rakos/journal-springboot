package com.example.trading.trading.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.trading.trading.dto.CreateAccountDTO;
import com.example.trading.trading.dto.UpdateAccountDTO;
import com.example.trading.trading.exceptions.BusinessRuleException;
import com.example.trading.trading.exceptions.ResourceNotFoundException;
import com.example.trading.trading.models.Account;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.repositories.AccountRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    public Account createAccount(CreateAccountDTO dto) {
        log.info("[START] AccountService -> createAccount");
        if (repo.existsByName(dto.getName())) {
            throw new BusinessRuleException("Account name already exists: " + dto.getName());
        }

        if (dto.getBalance() == null || dto.getBalance() < 0) {
            throw new BusinessRuleException("Initial balance cannot be negative: " + dto.getBalance());
        }

        Account account = new Account(dto.getName(), dto.getBalance());
        log.info("[END] AccountService -> createAccount");
        return repo.save(account);
    }

    public Optional<Account> getById(Long id) {
        log.info("[START] AccountService -> getById");
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Account with id " + id + " not found");
        }
        log.info("[END] AccountService -> getById");
        return repo.findById(id);
    }

    public List<Account> getAllAccounts() {
        log.info("[START] AccountService -> getAllAccounts");
        log.info("[END] AccountService --> getAllAccounts");
        return repo.findAll();
    }

    public void deleteAccount(Long id) {
        log.info("[START] AccountService -> deleteAccount");
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Account not found");
        }

        log.info("[END] AccountService --> deleteAccount");
        repo.deleteById(id);
    }

    public Account updateAccount(Long id, UpdateAccountDTO updatedAccount) {
        log.info("[START] AccountService -> updateAccount");
        boolean changed = false;
        Account account = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if (updatedAccount.getName() != null && !updatedAccount.getName().equals(account.getName())) {
            Account existAccountSameName = repo.existsByName(updatedAccount.getName())
                    ? repo.findByName(updatedAccount.getName())
                    : null;
            if (existAccountSameName != null && existAccountSameName.getId().longValue() != id.longValue()) {
                throw new BusinessRuleException("Account name already exists: " + updatedAccount.getName());
            }
            account.setName(updatedAccount.getName());
            changed = true;
        }

        if (updatedAccount.getBalance() != null
                && updatedAccount.getBalance().doubleValue() != account.getBalance().doubleValue()) {
            if (updatedAccount.getBalance() < 0) {
                throw new BusinessRuleException("Balance cannot be negative: " + updatedAccount.getBalance());
            }
            account.setBalance(updatedAccount.getBalance());
            changed = true;
        }

        if (updatedAccount.getActive() != null && updatedAccount.getActive() != account.isActive()) {
            account.setActive(updatedAccount.getActive());
            changed = true;
        }
        if (!changed) {
            throw new BusinessRuleException("No fields to update");
        }
        log.info("[END] AccountService --> updateAccount");
        return repo.save(account);
    }

    public List<Trade> getTradesByAccountId(Long id) {
        log.info("[START] AccountService -> getById");
        Account account = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found " + id));
        Set<Trade> trades = account.getTrades();
        log.info("[END] AccountService --> getTradesByAccountId");
        return new ArrayList<>(trades);
    }

}
