package com.finance.tracker.dto.account;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating account details (partial updates allowed)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateRequest {
    
    @Size(max = 255, message = "Account name cannot exceed 255 characters")
    private String name;
}