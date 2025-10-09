package com.example.trading.trading.dto.trade;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.trading.trading.dto.CreateTradeDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class CreateTradeDTOTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private CreateTradeDTO createValiTradeDTO() {
        CreateTradeDTO dto = new CreateTradeDTO();
        dto.setSymbol("EURUSD");
        dto.setType("long");
        dto.setStrategy("Scalping");
        dto.setSession("london");
        dto.setFeelings(List.of("confident", "nervous"));
        dto.setResult("win");
        dto.setDate(LocalDate.now());
        dto.setAccountIds(List.of(1L, 2L));
        return dto;
    }

    // Symbol tests
    @Test
    void testWithNullSymbol() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSymbol(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Symbol is required");
    }

    @Test
    void testWithEmptySymbol() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSymbol("");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Symbol is required");
    }

    @Test
    void testWithBlankSymbol() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSymbol("   ");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Symbol is required");
    }

    @Test
    void testWithShortSymbol() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSymbol("EU");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Symbol must be between 3 and 7 characters");
    }

    @Test
    void testWithLongSymbol() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSymbol("BTCUSDT");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithTooLongSymbol() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSymbol("BITCOINUSD");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Symbol must be between 3 and 7 characters");
    }

    // Type tests
    @Test
    void testWithNullType() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setType(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type is required");
    }

    @Test
    void testWithEmptyType() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setType("");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type is required");
    }

    @Test
    void testWithBlankType() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setType("   ");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type is required");
    }

    @Test
    void testWithInvalidType() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setType("medium");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type must be 'short', 'long', or 'long and short'");
    }

    @Test
    void testWithInvalidType2() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setType("longshort");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type must be 'short', 'long', or 'long and short'");
    }

    @Test
    void testWithValidTypeDifferentCase() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setType("LoNg");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithValidTypeLongAndShort() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setType("long and short");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Strategy tests
    @Test
    void testWithNullStrategy() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setStrategy(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Strategy is required");
    }

    @Test
    void testWithEmptyStrategy() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setStrategy("");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Strategy is required");
    }

    @Test
    void testWithBlankStrategy() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setStrategy("   ");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Strategy is required");
    }

    // Session tests
    @Test
    void testWithNullSession() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSession(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Session is required");
    }

    @Test
    void testWithEmptySession() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSession("");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Session is required");
    }

    @Test
    void testWithBlankSession() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSession("   ");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Session is required");
    }

    @Test
    void testWithInvalidSession() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSession("afternoon");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Type must be 'london', 'new york', 'asia', or 'full day'");
    }

    @Test
    void testWithValidSessionDifferentCase() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setSession("LoNdOn");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Feelings tests

    @Test
    void testWithNullFeelings() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setFeelings(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Feelings cannot be empty");
    }

    @Test
    void testWithEmptyFeelings() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setFeelings(List.of());
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Feelings cannot be empty");
    }

    @Test
    void testWithValidFeelings() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setFeelings(List.of("excited"));
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Result tests
    @Test
    void testWithNullResult() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setResult(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Result is required");
    }

    @Test
    void testWithEmptyResult() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setResult("");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Result is required");
    }

    @Test
    void testWithBlankResult() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setResult("   ");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Result is required");
    }

    @Test
    void testWithInvalidResult() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setResult("partial win");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Result must be 'win', 'loss', or 'breakeven'");
    }

    @Test
    void testWithValidResult() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setResult("loss");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithValidResult2() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setResult("breakeven");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithValidResult3() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setResult("LoSs");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Comment tests
    @Test
    void testWithNullComment() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setComment(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithEmptyComment() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setComment("");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithBlankComment() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setComment("   ");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithTooLongComment() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setComment("A".repeat(251));
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Comment cannot exceed 250 characters");
    }

    // Screenshoot tests

    @Test
    void testWithNullScreenshoot() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setScreenshoot(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithEmptyScreenshoot() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setScreenshoot("");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithBlankScreenshoot() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setScreenshoot("   ");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Screenshoot must be a valid URL");
    }

    @Test
    void testWithInvalidScreenshoot() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setScreenshoot("invalid-url");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Screenshoot must be a valid URL");
    }

    @Test
    void testWithValidScreenshoot() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setScreenshoot("https://example.com/screenshot.png");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // State tests
    @Test
    void testWithNullState() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setState(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithEmptyState() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setState("");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithBlankState() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setState("   ");
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithTooLongState() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setState("A".repeat(26));
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("State cannot exceed 25 characters");
    }

    // TP tests are not needed as they are Booleans without constraints
    // Date tests
    @Test
    void testWithNullDate() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setDate(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Date is required");
    }

    @Test
    void testWithFutureDate() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("Date cannot be in the future");
    }

    @Test
    void testWithTodayDate() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setDate(LocalDate.now());
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    @Test
    void testWithPastDate() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setDate(LocalDate.now().minusDays(10));
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    // Account IDs tests

    @Test
    void testWithNullAccountIds() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setAccountIds(null);
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("At least one account ID is required");
    }

    @Test
    void testWithEmptyAccountIds() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setAccountIds(List.of());
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).extracting("message").contains("At least one account ID is required");
    }

    @Test
    void testWithValidAccountIds() {
        CreateTradeDTO dto = createValiTradeDTO();
        dto.setAccountIds(List.of(5L));
        Set<ConstraintViolation<CreateTradeDTO>> errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }
}
