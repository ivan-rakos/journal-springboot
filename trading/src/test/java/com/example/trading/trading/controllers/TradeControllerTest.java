package com.example.trading.trading.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.trading.trading.TestDataFactory;
import com.example.trading.trading.dto.CreateTradeDTO;
import com.example.trading.trading.dto.UpdateTradeDTO;
import com.example.trading.trading.exceptions.ResourceNotFoundException;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.services.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TradeController.class)
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Get trade tests

    @Test
    void getAllTradesSuccess() throws Exception {
        List<Trade> trades = TestDataFactory.getTradeList();
        when(tradeService.getAllTrades()).thenReturn(trades);

        mockMvc.perform(get("/api/trades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(trades.size()))
                .andExpect(jsonPath("$[0].id").value(trades.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(trades.get(1).getId()));

        verify(tradeService).getAllTrades();
    }

    @Test
    void getAllTradesEmpty() throws Exception {
        when(tradeService.getAllTrades()).thenReturn(List.of());

        mockMvc.perform(get("/api/trades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(tradeService).getAllTrades();
    }

    @Test
    void getTradeByIdSuccess() throws Exception {
        Trade trade = TestDataFactory.createValidTradeWithAccount();
        when(tradeService.getTradeById(1L)).thenReturn(Optional.of(trade));

        mockMvc.perform(get("/api/trades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(trade.getId()))
                .andExpect(jsonPath("$.type").value(trade.getType()))
                .andExpect(jsonPath("$.symbol").value(trade.getSymbol()));

        verify(tradeService).getTradeById(1L);
    }

    @Test
    void getTradeByIdNotFound() throws Exception {
        when(tradeService.getTradeById(1L)).thenThrow(new ResourceNotFoundException("Trade with id 1 not found"));

        mockMvc.perform(get("/api/trades/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Trade with id 1 not found"));

        verify(tradeService).getTradeById(1L);
    }

    // Post trade tests

    @Test
    void createTradeSuccess() throws Exception {
        CreateTradeDTO createTradeDTO = TestDataFactory.createValidTradeDTO();
        Trade trade = TestDataFactory.createValidTradeWithAccount();
        when(tradeService.addTrade(any(CreateTradeDTO.class))).thenReturn(trade);

        mockMvc.perform(post("/api/trades")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createTradeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(trade.getId()))
                .andExpect(jsonPath("$.type").value(trade.getType()))
                .andExpect(jsonPath("$.symbol").value(trade.getSymbol()));

        verify(tradeService).addTrade(any(CreateTradeDTO.class));
    }

    @Test
    void createTradeInvalidData() throws Exception {
        CreateTradeDTO createTradeDTO = TestDataFactory.createValidTradeDTO();
        createTradeDTO.setType("LONGGGG");
        mockMvc.perform(post("/api/trades")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createTradeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.type").value("Type must be 'short', 'long', or 'long and short'"));

    }

    @Test
    void createTradeWithInexistentAccount() throws Exception {
        CreateTradeDTO createTradeDTO = TestDataFactory.createValidTradeDTO();
        when(tradeService.addTrade(any(CreateTradeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Account not found"));

        mockMvc.perform(post("/api/trades")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createTradeDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account not found"));

        verify(tradeService).addTrade(any(CreateTradeDTO.class));

    }

    // Patch trade tests

    @Test
    void updateTradeSuccess() throws Exception {
        UpdateTradeDTO updateTradeDTO = new UpdateTradeDTO();
        updateTradeDTO.setComment("Break and retest");
        Trade updatedTrade = TestDataFactory.createValidTrade();
        updatedTrade.setComment("Break and retest");

        when(tradeService.updateTrade(1L, updateTradeDTO)).thenReturn(updatedTrade);

        mockMvc.perform(patch("/api/trades/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateTradeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value(updatedTrade.getType()))
                .andExpect(jsonPath("$.comment").value(updateTradeDTO.getComment()))
                .andExpect(jsonPath("$.state").value(updatedTrade.getState()));

        verify(tradeService).updateTrade(1L, updateTradeDTO);
    }

    @Test
    void updateTradeInvalidData() throws Exception {
        UpdateTradeDTO updateTradeDTO = new UpdateTradeDTO();
        updateTradeDTO.setResult("winner");

        mockMvc.perform(patch("/api/trades/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateTradeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.result").value("Result must be 'win', 'loss', or 'breakeven'"));
    }

    @Test
    void updateTradeNotFoundId() throws Exception {
        UpdateTradeDTO updateTradeDTO = new UpdateTradeDTO();
        updateTradeDTO.setComment("Break and Retest");

        when(tradeService.updateTrade(1L, updateTradeDTO))
                .thenThrow(new ResourceNotFoundException("Trade not found 1"));

        mockMvc.perform(patch("/api/trades/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateTradeDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Trade not found 1"));

        verify(tradeService).updateTrade(1L, updateTradeDTO);
    }

    // Delete trade tests

    @Test
    void deleteTradeSuccess() throws Exception {
        mockMvc.perform(delete("/api/trades/1"))
                .andExpect(status().isNoContent());

        verify(tradeService).deleteTradeById(1L);
    }

    @Test
    void deleteTradeNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Trade not found 1")).when(tradeService).deleteTradeById(1L);

        mockMvc.perform(delete("/api/trades/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Trade not found 1"));

        verify(tradeService).deleteTradeById(1L);
    }

}
