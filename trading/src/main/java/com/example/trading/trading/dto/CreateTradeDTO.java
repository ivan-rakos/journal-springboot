package com.example.trading.trading.dto;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTradeDTO {
    @NotBlank(message = "Symbol is required")
    @Size(min = 3, max = 7, message = "Symbol must be between 3 and 7 characters")
    private String symbol;
    @NotNull(message = "Type is required")
    @Pattern(regexp = "^(?i)(short|long|long and short)$", message = "Type must be 'short', 'long', or 'long and short'")
    private String type;
    @NotBlank(message = "Strategy is required")
    private String strategy;
    @NotBlank(message = "Session is required")
    private String session;
    @NotEmpty(message = "Feelings cannot be empty")
    private List<String> feelings;
    @NotNull(message = "Result is required")
    @Pattern(regexp = "^(?i)(win|loss|breakeven)$", message = "Result must be 'win', 'loss', or 'breakeven'")
    private String result;
    private String comment;
    @URL(message = "Screenshoot must be a valid URL")
    private String screenshoot;
    private String state;
    private Boolean tp1;
    private Boolean tp2;
    private Boolean tp3;
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotEmpty(message = "At least one account ID is required")
    private List<Long> accountIds;

}
