package com.finance.tracker.dto.account;

import com.finance.tracker.entity.enums.AccountType;
import com.finance.tracker.entity.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new account
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRequest {
    
    private Long bankId; // Optional - will use default bank if not provided
    
    @NotBlank(message = "Account name is required")
    @Size(max = 255)
    private String name;
    
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    @NotNull(message = "Currency is required")
    private Currency currency;
}