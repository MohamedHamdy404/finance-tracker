package com.finance.tracker.dto.allocation;

import com.finance.tracker.entity.enums.AllocationStatus;
import com.finance.tracker.entity.enums.ContainerType;
import com.finance.tracker.entity.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AllocationResponse {
    private Long id;
    private Long accountId;
    private String accountName;
    private ContainerType containerType;
    private String name;
    private BigDecimal amount;
    private Currency currency;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private AllocationStatus allocationStatus;
    private String notes;
}
