package com.example.trading.trading.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String symbol;
    private String type;
    private String strategy;
    private String session;

    @ElementCollection
    private List<String> feelings = new ArrayList<>();

    private String result;
    private String comment;
    private String screenshoot;
    private String state;
    private boolean tp1;
    private boolean tp2;
    private boolean tp3;
    private LocalDate date;

    @ManyToMany(mappedBy = "trades")
    @ToString.Exclude
    private Set<Account> accounts = new HashSet<>();

}
