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

    private TransactionType transactionType; // Optional - will be inferred if not provided

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Digits(integer = 13, fraction = 2, message = "Amount must have max 13 digits before decimal and 2 after")
    private BigDecimal amount;

    private Currency currency; // Optional - will use account currency if not provided

    private LocalDate transactionDate; // Optional - will use current date if not provided

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description; // Optional - will use default if not provided

    @Positive(message = "FX rate must be positive if provided")
    @Digits(integer = 4, fraction = 6, message = "FX rate must have max 4 digits before decimal and 6 after")
    private BigDecimal fxRateToBase;

    private String notes;
}
