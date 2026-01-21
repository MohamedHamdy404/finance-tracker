package com.finance.tracker.entity;

import com.finance.tracker.entity.enums.Currency;
import com.finance.tracker.entity.enums.TransactionType;
import com.finance.tracker.entity.enums.TransferDirection;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction entity - Represents all financial transactions
 * Types: INCOME, EXPENSE, TRANSFER, ADJUSTMENT
 *
 * For TRANSFER: creates 2 linked rows with same transferGroupId
 * - Row 1: direction=OUT, accountId=fromAccount
 * - Row 2: direction=IN, accountId=toAccount
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transactions_user_id", columnList = "user_id"),
    @Index(name = "idx_transactions_account_id", columnList = "account_id"),
    @Index(name = "idx_transactions_category_id", columnList = "category_id"),
    @Index(name = "idx_transactions_date", columnList = "transaction_date"),
    @Index(name = "idx_transactions_user_date", columnList = "user_id, transaction_date"),
    @Index(name = "idx_transactions_type", columnList = "transaction_type"),
    @Index(name = "idx_transactions_transfer_group", columnList = "transfer_group_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Account is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_direction", length = 3)
    private TransferDirection transferDirection;

    @Column(name = "transfer_group_id")
    private UUID transferGroupId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    @NotNull(message = "Transaction date is required")
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull(message = "Description is required")
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "fx_rate_to_base", precision = 10, scale = 6)
    private BigDecimal fxRateToBase;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ========================================
    // Utility Methods
    // ========================================

    /**
     * Check if this is a transfer transaction
     */
    public boolean isTransfer() {
        return transactionType == TransactionType.TRANSFER;
    }

    /**
     * Check if this transaction affects income/expense reports
     */
    public boolean affectsReports() {
        return transactionType == TransactionType.INCOME ||
               transactionType == TransactionType.EXPENSE;
    }

    /**
     * Validate transfer constraints
     */
    @PrePersist
    @PreUpdate
    private void validateTransferConstraints() {
        if (transactionType == TransactionType.TRANSFER) {
            if (transferDirection == null) {
                throw new IllegalStateException("Transfer direction is required for TRANSFER transactions");
            }
            if (transferGroupId == null) {
                throw new IllegalStateException("Transfer group ID is required for TRANSFER transactions");
            }
        } else {
            if (transferDirection != null || transferGroupId != null) {
                throw new IllegalStateException("Transfer direction and group ID must be null for non-TRANSFER transactions");
            }
        }
    }
}
