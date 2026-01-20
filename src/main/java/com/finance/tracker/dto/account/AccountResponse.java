package com.finance.tracker.dto.account;

import com.finance.tracker.entity.enums.AccountType;
import com.finance.tracker.entity.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for account response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    
    private Long id;
    private Long bankId;
    private String bankName;
    private String name;
    private AccountType accountType;
    private Currency currency;
    private Boolean isActive;
    private String displayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
