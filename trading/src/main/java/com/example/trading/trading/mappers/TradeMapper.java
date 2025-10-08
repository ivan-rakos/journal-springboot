package com.example.trading.trading.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.trading.trading.dto.AccountDTO;
import com.example.trading.trading.dto.TradeDTO;
import com.example.trading.trading.dto.TradeSummaryDTO;
import com.example.trading.trading.models.Trade;

public class TradeMapper {

    public static TradeSummaryDTO tradeToSummaryDTO(Trade trade) {
        TradeSummaryDTO dto = new TradeSummaryDTO();
        dto.setId(trade.getId());
        dto.setSymbol(trade.getSymbol());
        dto.setType(trade.getType());
        dto.setStrategy(trade.getStrategy());
        dto.setSession(trade.getSession());
        dto.setFeelings(trade.getFeelings());
        dto.setResult(trade.getResult());
        dto.setComment(trade.getComment());
        dto.setScreenshoot(trade.getScreenshoot());
        dto.setState(trade.getState());
        dto.setTp1(trade.isTp1());
        dto.setTp2(trade.isTp2());
        dto.setTp3(trade.isTp3());
        dto.setDate(trade.getDate());

        return dto;
    }

    public static TradeDTO tradeToDTO(Trade trade) {
        TradeSummaryDTO tradeSummary = tradeToSummaryDTO(trade);
        TradeDTO dto = new TradeDTO(tradeSummary);

        Set<AccountDTO> accountDTOs = trade.getAccounts().stream().map(account -> {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(account.getId());
            accountDTO.setName(account.getName());
            accountDTO.setBalance(account.getBalance());
            return accountDTO;
        }).collect(Collectors.toSet());

        dto.setAccount(accountDTOs);

        return dto;
    }

    public static List<TradeSummaryDTO> tradeToSummaryDTOList(List<Trade> trades) {
        return trades.stream().map(TradeMapper::tradeToSummaryDTO).toList();
    }

    public static List<TradeDTO> tradeToDTOList(List<Trade> trades) {
        return trades.stream().map(TradeMapper::tradeToDTO).toList();
    }

}
