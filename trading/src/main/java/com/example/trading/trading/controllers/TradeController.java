package com.example.trading.trading.controllers;

import java.util.List;
import java.util.Optional;

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

import com.example.trading.trading.dto.CreateTradeDTO;
import com.example.trading.trading.dto.TradeDTO;
import com.example.trading.trading.dto.UpdateTradeDTO;
import com.example.trading.trading.mappers.TradeMapper;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.services.TradeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/trades")
public class TradeController {
    private final TradeService service;

    public TradeController(TradeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TradeDTO>> getAllTrades() {
        List<Trade> trades = service.getAllTrades();
        return ResponseEntity.status(HttpStatus.OK).body(TradeMapper.tradeToDTOList(trades));
    }

    @GetMapping("/{tradeId}")
    public ResponseEntity<TradeDTO> getTradeById(@PathVariable Long tradeId) {
        Optional<Trade> trade = service.getTradeById(tradeId);
        return ResponseEntity.status(HttpStatus.OK).body(TradeMapper.tradeToDTO(trade.get()));
    }

    @PostMapping
    public ResponseEntity<TradeDTO> createTrade(@Valid @RequestBody CreateTradeDTO tradeDTO) {
        Trade trade = service.addTrade(tradeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(TradeMapper.tradeToDTO(trade));
    }

    @PatchMapping("/{tradeId}")
    public ResponseEntity<TradeDTO> updateTrade(@PathVariable Long tradeId, @Valid @RequestBody UpdateTradeDTO trade) {
        Trade tradeUpdated = service.updateTrade(tradeId, trade);
        return ResponseEntity.status(HttpStatus.OK).body(TradeMapper.tradeToDTO(tradeUpdated));
    }

    @DeleteMapping("/{tradeId}")
    public ResponseEntity<Void> deleteTrade(@PathVariable Long tradeId) {
        service.deleteTradeById(tradeId);
        return ResponseEntity.noContent().build();
    }

}
