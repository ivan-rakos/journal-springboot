package com.example.trading.trading.dto.trade;

import java.util.Set;

import com.example.trading.trading.dto.UpdateTradeDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class UpdateTradeDTOTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private UpdateTradeDTO createValidDTO() {
        UpdateTradeDTO dto = new UpdateTradeDTO();
        dto.setSymbol(null);
        dto.setType(null);
        dto.setStrategy(null);
        dto.setSession(null);
        dto.setFeelings(null);
        dto.setResult(null);
        dto.setComment(null);
        dto.setScreenshoot(null);
        dto.setState(null);
        dto.setTp1(null);
        dto.setTp2(null);
        dto.setTp3(null);
        dto.setDate(null);
        dto.setAccountIds(null);
        return dto;
    }

    // Null or empty fields are allowed because it's an update DTO, so no tests for
    // that

    // Symbol Tests
    @Test
    void testWithBlankSymbol() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setSymbol("   ");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Symbol must be between 3 and 7 characters");
    }

    @Test
    void testWithShortSymbol() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setSymbol("EU");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Symbol must be between 3 and 7 characters");
    }

    @Test
    void testWithLongSymbol() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setSymbol("EUROUSDX");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Symbol must be between 3 and 7 characters");
    }

    @Test
    void testWithValidSymbol() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setSymbol("EURUSD");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Type tests
    @Test
    void testWithBlankType() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setType("    ");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type must be 'short', 'long', or 'long and short'");
    }

    @Test
    void testWithInvalidType() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setType("medium");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type must be 'short', 'long', or 'long and short'");
    }

    @Test
    void testWithInvalidType2() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setType("longshort");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type must be 'short', 'long', or 'long and short'");
    }

    @Test
    void testWithValidTypeDifferentCase() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setType("LoNg AnD ShOrT");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithValidType() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setType("short");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Strategy tests
    @Test
    void testWithBlankStrategy() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setStrategy("   ");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Strategy can only contain letters, numbers, and spaces");
    }

    // Session tests
    @Test
    void testWithBlankSession() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setSession("    ");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type must be 'london', 'new york', 'asia', or 'full day'");
    }

    @Test
    void testWithInvalidSession() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setSession("afternoon");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type must be 'london', 'new york', 'asia', or 'full day'");
    }

    @Test
    void testWithValidSessionDifferentCase() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setSession("AsIa");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithValidSession() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setSession("full day");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Feelings tests - no constraints to test

    // Result tests
    @Test
    void testWithBlankResult() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setResult("   ");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Result must be 'win', 'loss', or 'breakeven'");
    }

    @Test
    void testWithInvalidResult() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setResult("profit");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Result must be 'win', 'loss', or 'breakeven'");
    }

    @Test
    void testWithValidResultDifferentCase() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setResult("BrEaKeVen");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithValidResult() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setResult("win");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Comment tests
    @Test
    void testWithLongComment() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setComment("A".repeat(251));
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Comment cannot exceed 250 characters");
    }

    @Test
    void testWithValidComment() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setComment("This is a valid comment.");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Screenshoot tests
    @Test
    void testWithBlankScreenshoot() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setScreenshoot("   ");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Screenshoot must be a valid URL");
    }

    @Test
    void testWithInvalidScreenshoot() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setScreenshoot("invalid-url");
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Screenshoot must be a valid URL");
    }

    // State tests
    @Test
    void testWithLongState() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setState("A".repeat(26));
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("State cannot exceed 25 characters");
    }

    // TP tests are not needed as they are Booleans without constraints
    // Date tests
    @Test
    void testWithFutureDate() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setDate(java.time.LocalDate.now().plusDays(1));
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Date cannot be in the future");
    }

    @Test
    void testWithPastDate() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setDate(java.time.LocalDate.now().minusDays(1));
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithTodayDate() {
        UpdateTradeDTO dto = createValidDTO();
        dto.setDate(java.time.LocalDate.now());
        Set<ConstraintViolation<UpdateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Account IDs tests - no constraints to test
}
