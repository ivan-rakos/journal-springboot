package com.example.trading.trading.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.List;
import lombok.Data;

@Data
public class UpdateTradeDTO {
    private String symbol;
    private String type;
    private String strategy;
    private String session;
    private List<String> feelings;
    private String result;
    private String comment;
    private String screenshoot;
    private String state;
    private Boolean tp1;
    private Boolean tp2;
    private Boolean tp3;
    private LocalDate date;
    private List<Long> accountIds;

}
