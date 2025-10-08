package com.example.trading.trading.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s]+$", message = "Name can only contain letters, numbers, and spaces")
    private String name;
    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be non-negative")
    @Digits(integer = 7, fraction = 2, message = "Balance must be a valid monetary amount")
    private Double balance;

}
