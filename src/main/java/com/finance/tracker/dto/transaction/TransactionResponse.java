package com.finance.tracker.dto.transaction;

import com.finance.tracker.entity.enums.Currency;
import com.finance.tracker.entity.enums.TransactionType;
import com.finance.tracker.entity.enums.TransferDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for transaction response (all types)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private Long accountId;
    private String accountName;
    private Long categoryId;
    private String categoryName;
    private TransactionType transactionType;
    private TransferDirection transferDirection;
    private UUID transferGroupId;
    private BigDecimal amount;
    private Currency currency;
    private LocalDate transactionDate;
    private String description;
    private BigDecimal fxRateToBase;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
