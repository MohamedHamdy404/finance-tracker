package com.finance.tracker.dto.transaction;

import com.finance.tracker.entity.enums.Currency;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating TRANSFER transactions
 * Creates 2 linked transactions: OUT from source account, IN to destination account
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferCreateRequest {

    @NotNull(message = "From account ID is required")
    private Long fromAccountId;

    @NotNull(message = "To account ID is required")
    private Long toAccountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Digits(integer = 13, fraction = 2, message = "Amount must have max 13 digits before decimal and 2 after")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @NotNull(message = "Transfer date is required")
    private LocalDate transferDate;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Positive(message = "FX rate must be positive if provided")
    @Digits(integer = 4, fraction = 6, message = "FX rate must have max 4 digits before decimal and 6 after")
    private BigDecimal fxRateToBase;

    private String notes;
}
