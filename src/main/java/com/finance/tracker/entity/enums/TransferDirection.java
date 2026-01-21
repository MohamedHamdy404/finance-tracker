package com.finance.tracker.entity.enums;

/**
 * Transfer direction enum (only for TRANSFER transactions)
 * Indicates whether money is leaving (OUT) or entering (IN) an account
 */
public enum TransferDirection {
    OUT("Outgoing Transfer"),
    IN("Incoming Transfer");

    private final String displayName;

    TransferDirection(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get opposite direction (for linked transfers)
     */
    public TransferDirection opposite() {
        return this == OUT ? IN : OUT;
    }
}
