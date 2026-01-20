package com.finance.tracker.entity.enums;

/**
 * Currency enum - Supported currencies
 */
public enum Currency {
    EGP("Egyptian Pound", "E£"),
    USD("US Dollar", "$");

    private final String displayName;
    private final String symbol;

    Currency(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }
}
