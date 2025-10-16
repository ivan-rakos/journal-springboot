package com.example.trading.trading.dto;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTradeDTO {
    @Size(min = 3, max = 7, message = "Symbol must be between 3 and 7 characters")
    private String symbol;
    @Pattern(regexp = "^(?i)(short|long|long and short)$", message = "Type must be 'short', 'long', or 'long and short'")
    private String type;
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s]+$", message = "Strategy can only contain letters, numbers, and spaces")
    private String strategy;
    @Pattern(regexp = "^(?i)(london|new york|asia|full day)$", message = "Type must be 'london', 'new york', 'asia', or 'full day'")
    private String session;
    private List<String> feelings;
    @Pattern(regexp = "^(?i)(win|loss|breakeven)$", message = "Result must be 'win', 'loss', or 'breakeven'")
    private String result;
    @Size(max = 250, message = "Comment cannot exceed 250 characters")
    private String comment = "";
    @URL(message = "Screenshoot must be a valid URL")
    private String screenshoot;
    @Size(min = 3, max = 25, message = "State must be between 3 and 25 characters")
    private String state = "";
    private Boolean tp1;
    private Boolean tp2;
    private Boolean tp3;
    @PastOrPresent(message = "Date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<Long> accountIds;

    public void setSymbol(String symbol) {
        if (symbol != null) {
            this.symbol = symbol.trim();
        } else {
            this.symbol = null;
        }
    }

    public void setStrategy(String strategy) {
        if (strategy != null) {
            this.strategy = strategy.trim();
        } else {
            this.strategy = null;
        }
    }

    public void setComment(String comment) {
        if (comment != null) {
            this.comment = comment.trim();
        } else {
            this.comment = null;
        }
    }

    public void setState(String state) {
        if (state != null) {
            this.state = state.trim();
        } else {
            this.state = null;
        }
    }
}
