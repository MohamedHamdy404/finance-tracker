package com.finance.tracker.entity.enums;

/**
 * Account type enum
 */
public enum AccountType {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}