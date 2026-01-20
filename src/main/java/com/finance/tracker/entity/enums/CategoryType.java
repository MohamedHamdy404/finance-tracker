package com.finance.tracker.entity.enums;

/**
 * Category type enum - Used to classify categories as income or expense
 */
public enum CategoryType {
    INCOME("Income"),
    EXPENSE("Expense");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}