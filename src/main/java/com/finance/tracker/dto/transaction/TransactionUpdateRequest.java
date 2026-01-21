package com.finance.tracker.dto.transaction;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for updating transaction (partial updates allowed)
 * Cannot change transactionType, transferDirection, or transferGroupId
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionUpdateRequest {

    private Long categoryId;

    @Positive(message = "Amount must be positive if provided")
    @Digits(integer = 13, fraction = 2, message = "Amount must have max 13 digits before decimal and 2 after")
    private BigDecimal amount;

    private LocalDate transactionDate;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Positive(message = "FX rate must be positive if provided")
    @Digits(integer = 4, fraction = 6, message = "FX rate must have max 4 digits before decimal and 6 after")
    private BigDecimal fxRateToBase;

    private String notes;
}
