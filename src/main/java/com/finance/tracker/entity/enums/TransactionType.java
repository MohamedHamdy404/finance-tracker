package com.finance.tracker.entity.enums;

/**
 * Transaction type enum
 * Defines the nature of financial transactions
 */
public enum TransactionType {
    INCOME("Income"),
    EXPENSE("Expense"),
    TRANSFER("Transfer"),
    ADJUSTMENT("Adjustment");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Check if this transaction type affects income/expense reports
     */
    public boolean affectsReports() {
        return this == INCOME || this == EXPENSE;
    }
}
