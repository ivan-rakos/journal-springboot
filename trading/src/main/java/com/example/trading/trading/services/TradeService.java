package com.example.trading.trading.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.trading.trading.dto.CreateTradeDTO;
import com.example.trading.trading.dto.UpdateTradeDTO;
import com.example.trading.trading.models.Account;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.repositories.AccountRepository;
import com.example.trading.trading.repositories.TradeRepository;

@Service
public class TradeService {

    private final TradeRepository repo;
    private final AccountRepository accountRepo;

    public TradeService(TradeRepository repo, AccountRepository accountRepo) {
        this.repo = repo;
        this.accountRepo = accountRepo;
    }

    public List<Trade> getAllTrades() {
        return repo.findAll();
    }

    public Optional<Trade> getTradeById(Long tradeId) {
        return repo.findById(tradeId);
    }

    public Trade addTrade(CreateTradeDTO tradeDTO) {
        Trade trade = new Trade();
        trade.setSymbol(tradeDTO.getSymbol());
        trade.setType(tradeDTO.getType());
        trade.setStrategy(tradeDTO.getStrategy());
        trade.setSession(tradeDTO.getSession());
        trade.setFeelings(tradeDTO.getFeelings());
        trade.setResult(tradeDTO.getResult());
        trade.setComment(tradeDTO.getComment());
        trade.setScreenshoot(tradeDTO.getScreenshoot());
        trade.setState(tradeDTO.getState());
        trade.setTp1(tradeDTO.getTp1());
        trade.setTp2(tradeDTO.getTp2());
        trade.setTp3(tradeDTO.getTp3());
        trade.setDate(tradeDTO.getDate());

        Set<Account> accounts = tradeDTO.getAccountIds().stream()
                .map(id -> accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Account not found " + id)))
                .collect(Collectors.toSet());
        trade.setAccounts(accounts);

        accounts.forEach(account -> account.getTrades().add(trade));
        return repo.save(trade);
    }

    public Trade updateTrade(Long id, UpdateTradeDTO dto) {
        Trade trade = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found " + id));

        if (dto.getSymbol() != null)
            trade.setSymbol(dto.getSymbol());
        if (dto.getType() != null)
            trade.setType(dto.getType());
        if (dto.getStrategy() != null)
            trade.setStrategy(dto.getStrategy());
        if (dto.getSession() != null)
            trade.setSession(dto.getSession());
        if (dto.getFeelings() != null)
            trade.setFeelings(dto.getFeelings());
        if (dto.getResult() != null)
            trade.setResult(dto.getResult());
        if (dto.getComment() != null)
            trade.setComment(dto.getComment());
        if (dto.getScreenshoot() != null)
            trade.setScreenshoot(dto.getScreenshoot());
        if (dto.getState() != null)
            trade.setState(dto.getState());
        if (dto.getTp1() != null)
            trade.setTp1(dto.getTp1());
        if (dto.getTp2() != null)
            trade.setTp2(dto.getTp2());
        if (dto.getTp3() != null)
            trade.setTp3(dto.getTp3());
        if (dto.getDate() != null)
            trade.setDate(dto.getDate());

        if (dto.getAccountIds() != null) {
            Set<Account> accounts = dto.getAccountIds().stream()
                    .map(idAccount -> accountRepo.findById(idAccount)
                            .orElseThrow(() -> new RuntimeException("Account not found " + idAccount)))
                    .collect(Collectors.toSet());

            // Remove trade from accounts that are no longer associated (update owning side)
            Set<Account> previousAccounts = trade.getAccounts() == null ? new java.util.HashSet<>()
                    : new java.util.HashSet<>(trade.getAccounts());
            for (Account previous : previousAccounts) {
                if (!accounts.contains(previous)) {
                    previous.getTrades().remove(trade);
                }
            }

            // Add trade to new accounts (ensure owning side is updated)
            for (Account account : accounts) {
                if (!account.getTrades().contains(trade)) {
                    account.getTrades().add(trade);
                }
            }

            // Update inverse side as well to keep in-memory state consistent
            trade.setAccounts(accounts);
        }
        return repo.save(trade);
    }

    public void deleteTradeById(Long id) {
        Trade trade = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade not found " + id));

        for (Account account : trade.getAccounts()) {
            account.getTrades().remove(trade);
        }
        trade.getAccounts().clear();

        repo.delete(trade);
    }

}
