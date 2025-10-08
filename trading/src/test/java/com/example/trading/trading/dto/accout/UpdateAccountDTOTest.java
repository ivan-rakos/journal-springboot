package com.example.trading.trading.dto.accout;

import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.trading.trading.dto.UpdateAccountDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class UpdateAccountDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testWithEmptyName() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName("");
        dto.setBalance(null);
        dto.setActive(null);
        Set<ConstraintViolation<UpdateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name must be between 3 and 50 characters");
    }

    @Test
    void testWithBlankName() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName("   ");
        dto.setBalance(null);
        dto.setActive(null);
        Set<ConstraintViolation<UpdateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name is required");
    }

    @Test
    void testWithShortName() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName("AB");
        dto.setBalance(null);
        dto.setActive(null);
        Set<ConstraintViolation<UpdateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name must be between 3 and 50 characters");
    }

    @Test
    void testWithLongName() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName("A".repeat(51));
        dto.setBalance(null);
        dto.setActive(null);
        Set<ConstraintViolation<UpdateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name must be between 3 and 50 characters");
    }

    @Test
    void testWithInvalidNameCharacters() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName("Invalid@Name!");
        dto.setBalance(null);
        dto.setActive(null);
        Set<ConstraintViolation<UpdateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Name can only contain letters, numbers, and spaces");
    }

    @Test
    void testWithNegativeBalance() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName(null);
        dto.setBalance(-50.0);
        dto.setActive(null);
        Set<ConstraintViolation<UpdateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Balance must be non-negative");
    }

    @Test
    void testWithInvalidBalanceFormat() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName(null);
        dto.setBalance(10000000.0); // More than 7 integer digits
        dto.setActive(null);
        Set<ConstraintViolation<UpdateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Balance must be a valid monetary amount");
    }

    @Test
    void testWithMinimumBalance() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName(null);
        dto.setBalance(10.0);
        dto.setActive(null);
        Set<ConstraintViolation<UpdateAccountDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

}
