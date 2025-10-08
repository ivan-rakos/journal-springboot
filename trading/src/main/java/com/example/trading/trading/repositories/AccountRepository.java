package com.example.trading.trading.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading.trading.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
