package com.example.trading.trading.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private Long id;
    private String name;
    private Double balance;
    private boolean active;

}
