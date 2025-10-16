package com.example.trading.trading.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.trading.trading.TestDataFactory;
import com.example.trading.trading.dto.CreateTradeDTO;
import com.example.trading.trading.dto.UpdateTradeDTO;
import com.example.trading.trading.exceptions.ResourceNotFoundException;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.repositories.AccountRepository;
import com.example.trading.trading.repositories.TradeRepository;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TradeService tradeService;

    // Create Trade Tests
    @Test
    public void testCreateValidTrade() {
        CreateTradeDTO dto = TestDataFactory.createValidTradeDTO();
        Trade trade = TestDataFactory.createValidTradeWithAccount();
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(TestDataFactory.createValidAccount(1L, "Test Account")));
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        Trade createdTrade = tradeService.addTrade(dto);
        assertNotNull(createdTrade);
        assertEquals(trade, createdTrade);
        verify(accountRepository).findById(1L);
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    public void testCreateTradeWithoutScreenshot() {
        CreateTradeDTO dto = TestDataFactory.createValidTradeDTO();
        dto.setScreenshoot(null);
        Trade trade = TestDataFactory.createValidTrade();
        trade.setScreenshoot(null);
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(TestDataFactory.createValidAccount(1L, "Test Account")));
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        Trade createdTrade = tradeService.addTrade(dto);
        assertNotNull(createdTrade);
        assertEquals(trade, createdTrade);
        verify(accountRepository).findById(1L);
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    public void testCreateTradeWithMultipleAccounts() {
        CreateTradeDTO dto = TestDataFactory.createValidTradeDTO();
        dto.setAccountIds(List.of(1L, 2L));
        Trade trade = TestDataFactory.createValidTrade();
        trade.setAccounts(Set.of(TestDataFactory.createValidAccount(1L, "Test Account"),
                TestDataFactory.createValidAccount(2L, "Second Account")));
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(TestDataFactory.createValidAccount(1L, "Test Account")));
        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(TestDataFactory.createValidAccount(2L, "Second Account")));
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        Trade createdTrade = tradeService.addTrade(dto);
        assertNotNull(createdTrade);
        assertEquals(trade, createdTrade);
        assertTrue(trade.getAccounts().size() == 2);
        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    public void testCreateTradeWithInexistentAccount() {
        CreateTradeDTO dto = TestDataFactory.createValidTradeDTO();
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tradeService.addTrade(dto));
        verify(accountRepository).findById(1L);
        verify(tradeRepository, never()).save(any(Trade.class));
    }

    // Update Trade Tests
    @Test
    public void testUpdateTrade_Success() {
        Long tradeId = 1L;
        Trade existingTrade = TestDataFactory.createValidTrade();
        UpdateTradeDTO dto = new UpdateTradeDTO();
        dto.setComment("Updated comment");
        when(tradeRepository.findById(tradeId)).thenReturn(Optional.of(existingTrade));
        when(tradeRepository.save(any(Trade.class))).thenReturn(existingTrade);
        Trade updatedTrade = tradeService.updateTrade(tradeId, dto);
        assertNotNull(updatedTrade);
        assertEquals(existingTrade.getComment(), updatedTrade.getComment());
        assertEquals(existingTrade.getDate(), updatedTrade.getDate());
        verify(tradeRepository).findById(tradeId);
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    public void testUpdateTrade_SetNullFields() {
        Long tradeId = 1L;
        Trade existingTrade = TestDataFactory.createValidTrade();
        UpdateTradeDTO dto = new UpdateTradeDTO();
        dto.setComment(null);
        dto.setScreenshoot(null);
        when(tradeRepository.findById(tradeId)).thenReturn(Optional.of(existingTrade));
        when(tradeRepository.save(any(Trade.class))).thenReturn(existingTrade);
        Trade updatedTrade = tradeService.updateTrade(tradeId, dto);
        assertNotNull(updatedTrade);
        assertNull(updatedTrade.getComment());
        assertNull(updatedTrade.getScreenshoot());
        verify(tradeRepository).findById(tradeId);
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    public void testUpdateTrade_NotFound() {
        Long tradeId = 1L;
        UpdateTradeDTO dto = new UpdateTradeDTO();
        when(tradeRepository.findById(tradeId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tradeService.updateTrade(tradeId, dto));
        verify(tradeRepository).findById(tradeId);
        verify(tradeRepository, never()).save(any(Trade.class));
    }

    // Get Trade Tests

    @Test
    public void testGetTradeById() {
        Long tradeId = 1L;
        Trade trade = TestDataFactory.createValidTrade();
        when(tradeRepository.existsById(tradeId)).thenReturn(true);
        when(tradeRepository.findById(tradeId)).thenReturn(Optional.of(trade));
        Optional<Trade> foundTrade = tradeService.getTradeById(tradeId);
        assertTrue(foundTrade.isPresent());
        assertEquals(trade, foundTrade.get());
        verify(tradeRepository).existsById(tradeId);
        verify(tradeRepository).findById(tradeId);
    }

    @Test
    public void testGetAllTrades() {
        Trade trade1 = TestDataFactory.createValidTrade();
        Trade trade2 = TestDataFactory.createValidTrade();
        trade2.setId(2L);
        when(tradeRepository.findAll()).thenReturn(List.of(trade1, trade2));
        List<Trade> trades = tradeService.getAllTrades();
        assertNotNull(trades);
        assertEquals(2, trades.size());
        verify(tradeRepository).findAll();
    }

    @Test
    public void testGetTradeById_NotFound() {
        Long tradeId = 1L;
        when(tradeRepository.existsById(tradeId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> tradeService.getTradeById(tradeId));
        verify(tradeRepository).existsById(tradeId);
        verify(tradeRepository, never()).findById(tradeId);
    }

    // Delete Trade Tests

    @Test
    public void testDeleteTrade_Success() {
        Long tradeId = 1L;
        Trade trade = TestDataFactory.createValidTradeWithAccount();
        when(tradeRepository.findById(tradeId)).thenReturn(Optional.of(trade));
        tradeService.deleteTradeById(tradeId);
        verify(tradeRepository).findById(tradeId);
        verify(tradeRepository).delete(trade);
    }

    @Test
    public void testDeleteTrade_NotFound() {
        Long tradeId = 1L;
        when(tradeRepository.findById(tradeId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tradeService.deleteTradeById(tradeId));
        verify(tradeRepository).findById(tradeId);
        verify(tradeRepository, never()).delete(any(Trade.class));
    }
}
