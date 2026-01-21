package com.finance.tracker.dto.allocation;

import com.finance.tracker.entity.enums.ContainerType;
import com.finance.tracker.entity.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AllocationCreateRequest {
    private Long accountId;

    @NotNull
    private ContainerType containerType;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    @NotNull
    private LocalDate startDate;

    private LocalDate maturityDate;

    private String notes;
}
