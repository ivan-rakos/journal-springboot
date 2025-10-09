package com.example.trading.trading.dto.accout;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.example.trading.trading.dto.CreateAccountDTO;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class CreateAccountDTOTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testWithoutName() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setBalance(100.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name is required");
    }

    @Test
    void testWithNullName() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName(null);
        dto.setBalance(100.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name is required");
    }

    @Test
    void testWithEmptyName() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("");
        dto.setBalance(100.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name is required");
    }

    @Test
    void testWithBlankName() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("   ");
        dto.setBalance(100.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name is required");
    }

    @Test
    void testWithShortName() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("AB");
        dto.setBalance(100.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name must be between 3 and 50 characters");
    }

    @Test
    void testWithLongName() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("A".repeat(51));
        dto.setBalance(100.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name must be between 3 and 50 characters");
    }

    @Test
    void testWithInvalidCharactersInName() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("Account@123");
        dto.setBalance(100.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name can only contain letters, numbers, and spaces");
    }

    @Test
    void testWithNegativeBalance() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("My Account");
        dto.setBalance(-50.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Balance must be non-negative");
    }

    @Test
    void testWithNullBalance() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("My Account");
        dto.setBalance(null);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Balance is required");
    }

    @Test
    void testWithMinimumBalance() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("My Account");
        dto.setBalance(0.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithInvalidBalanceFormat() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("My Account");
        dto.setBalance(10000000.0); // More than 7 integer digits
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Balance must be a valid monetary amount");
    }

    @Test
    void testWithValidData() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("My Account");
        dto.setBalance(150.0);
        Set<ConstraintViolation<CreateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

}
