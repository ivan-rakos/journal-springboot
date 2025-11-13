package com.example.trading.trading.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.trading.trading.dto.UpdateTradeDTO;
import com.example.trading.trading.repositories.TradeRepository;
import com.example.trading.trading.services.TradeService;

@Service
@RequiredArgsConstructor
@Slf4j
public class FixTradesStatusSchedule {

    private final TradeService tradeService;

    @Scheduled(cron = "0 0 0 * * *")
    public void fixTradesStatus() {
        log.info("[START SCHEDULE] Fixing trades status");
        List<Long> tradesClosed = new ArrayList<>();

        tradeService.getAllTrades().forEach(trade -> {
            String status = trade.getState();
            if (!status.contains("Close") && !status.contains("closing")) {
                LocalDate tradeDate = trade.getDate();
                if (tradeDate.isBefore(LocalDate.now()) && !trade.getSession().equals("full day")) {
                    UpdateTradeDTO dto = new UpdateTradeDTO();
                    dto.setState("Close by Schedule");
                    tradeService.updateTrade(trade.getId(), dto);
                    tradesClosed.add(trade.getId());
                }
            }
        });
        log.info("Closed trades are {} ", tradesClosed.toString());

        log.info("[END SCHEDULE] fixTradesStatus");
    }

}
