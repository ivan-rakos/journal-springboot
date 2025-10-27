package com.example.trading.trading;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.trading.trading.dto.CreateAccountDTO;
import com.example.trading.trading.dto.CreateTradeDTO;
import com.example.trading.trading.dto.UpdateAccountDTO;
import com.example.trading.trading.models.Account;
import com.example.trading.trading.models.Trade;

public class TestDataFactory {

    public static CreateAccountDTO createValidAccountDTO() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("Test Account");
        dto.setBalance(1000.00);
        return dto;
    }

    public static Account createValidAccount(Long id, String name) {
        Account account = new Account();
        account.setId(id);
        account.setName(name);
        account.setBalance(1000.00);
        account.setActive(true);
        account.setTrades(new HashSet<>(Set.of(createValidTrade())));
        return account;
    }

    public static UpdateAccountDTO createValidUpdateAccountDTO() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName("Updated Account");
        dto.setBalance(2000.00);
        dto.setActive(true);
        return dto;
    }

    public static Account updatedAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setName("Updated Account");
        account.setBalance(2000.00);
        account.setActive(true);
        return account;
    }

    public static List<Account> getAccountList() {
        return List.of(createValidAccount(1L, "Test Account"), updatedAccount());
    }

    public static Trade createValidTrade() {
        Trade trade = new Trade();
        trade.setId(1L);
        trade.setType("BUY");
        trade.setSymbol("EURUSD");
        trade.setStrategy("Scalping");
        trade.setSession("London");
        trade.setResult("WIN");
        trade.setFeelings(List.of("Confident", "Happy"));
        trade.setComment("Good trade");
        trade.setScreenshoot("https://www.tradingview.com/x/5pnHBH6y/");
        trade.setState("CLOSED");
        trade.setTp1(true);
        trade.setTp2(false);
        trade.setTp3(false);
        trade.setDate(LocalDate.now());
        // trade.setAccounts(Set.of(createValidAccount(1L, "Test Account")));
        return trade;
    }

    public static Trade createValidTradeWithAccount() {
        Trade trade = createValidTrade();
        trade.setAccounts(new HashSet<>(Set.of(createValidAccount(1L, "Test Account"))));
        return trade;
    }

    public static CreateTradeDTO createValidTradeDTO() {
        CreateTradeDTO dto = new CreateTradeDTO();
        dto.setType("SHORT");
        dto.setSymbol("EURUSD");
        dto.setStrategy("Scalping");
        dto.setSession("London");
        dto.setResult("WIN");
        dto.setFeelings(List.of("Confident", "Happy"));
        dto.setComment("Good trade");
        dto.setScreenshoot("https://www.tradingview.com/x/5pnHBH6y/");
        dto.setState("CLOSED");
        dto.setTp1(true);
        dto.setTp2(false);
        dto.setTp3(false);
        dto.setDate(LocalDate.now());
        dto.setAccountIds(List.of(1L));
        return dto;
    }

    public static List<Trade> getTradeList() {
        Trade trade1 = createValidTradeWithAccount();
        Trade trade2 = createValidTradeWithAccount();
        trade2.setId(2L);
        return List.of(trade1, trade2);
    }

}
