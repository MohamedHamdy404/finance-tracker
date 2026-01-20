package com.finance.tracker.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for bank response (read-only, from seed data)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponse {
    
    private Long id;
    private String name;
    private String code;
    private String country;
    private LocalDateTime createdAt;
}