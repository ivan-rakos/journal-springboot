package com.example.trading.trading.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading.trading.models.Trade;

public interface TradeRepository extends JpaRepository<Trade, Long> {

}
