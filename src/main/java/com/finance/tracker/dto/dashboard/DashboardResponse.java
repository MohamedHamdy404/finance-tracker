package com.finance.tracker.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {
    private BigDecimal totalWealth;
    private BigDecimal totalLiquidAssets;
    private BigDecimal totalAllocatedFunds;
    private Map<String, BigDecimal> wealthByCurrency;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private BigDecimal monthlySavings;
}
