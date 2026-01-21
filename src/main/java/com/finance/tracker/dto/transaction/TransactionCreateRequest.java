package com.finance.tracker.dto.transaction;

import com.finance.tracker.entity.enums.Currency;
import com.finance.tracker.entity.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Base DTO for creating transactions (INCOME, EXPENSE, ADJUSTMENT)
 * For TRANSFER, use TransferCreateRequest instead
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCreateRequest {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    private Long categoryId;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Digits(integer = 13, fraction = 2, message = "Amount must have max 13 digits before decimal and 2 after")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Positive(message = "FX rate must be positive if provided")
    @Digits(integer = 4, fraction = 6, message = "FX rate must have max 4 digits before decimal and 6 after")
    private BigDecimal fxRateToBase;

    private String notes;
}
