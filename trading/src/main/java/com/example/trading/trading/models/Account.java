package com.example.trading.trading.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private Double balance = 0.0;
    private boolean active = true;

    @ManyToMany
    @JoinTable(name = "account_trade", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "trade_id"))
    @ToString.Exclude
    private Set<Trade> trades = new HashSet<>();

    public Account(String name, Double balance) {
        this.name = name;
        this.balance = balance;
    }
}
