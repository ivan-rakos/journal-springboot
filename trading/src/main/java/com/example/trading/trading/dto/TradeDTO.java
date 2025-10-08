package com.example.trading.trading.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class TradeDTO {
    private Long id;
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

    private Set<AccountDTO> account;

    public TradeDTO(TradeSummaryDTO dto) {
        this.id = dto.getId();
        this.symbol = dto.getSymbol();
        this.type = dto.getType();
        this.strategy = dto.getStrategy();
        this.session = dto.getSession();
        this.feelings = dto.getFeelings();
        this.result = dto.getResult();
        this.comment = dto.getComment();
        this.screenshoot = dto.getScreenshoot();
        this.state = dto.getState();
        this.tp1 = dto.getTp1();
        this.tp2 = dto.getTp2();
        this.tp3 = dto.getTp3();
        this.date = dto.getDate();
    }

}
