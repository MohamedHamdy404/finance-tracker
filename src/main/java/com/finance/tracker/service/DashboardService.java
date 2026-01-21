package com.finance.tracker.service;

import com.finance.tracker.dto.dashboard.DashboardResponse;
import com.finance.tracker.entity.Account;
import com.finance.tracker.entity.Allocation;
import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.enums.TransactionType;
import com.finance.tracker.repository.AccountRepository;
import com.finance.tracker.repository.AllocationRepository;
import com.finance.tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AllocationRepository allocationRepository;

    public DashboardResponse getDashboardData(Long userId) {
        List<Account> accounts = accountRepository.findByUser_Id(userId);
        List<Allocation> allocations = allocationRepository.findByUser_Id(userId);
        
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        List<Transaction> monthlyTransactions = transactionRepository.findByUser_IdOrderByTransactionDateDesc(userId).stream()
                .filter(t -> !t.getTransactionDate().isBefore(firstDayOfMonth))
                .toList();

        BigDecimal totalLiquid = BigDecimal.ZERO;
        BigDecimal totalAllocated = BigDecimal.ZERO;
        Map<String, BigDecimal> wealthByCurrency = new HashMap<>();

        // Simplified calculation: assuming all accounts/allocations are in EGP for total wealth
        // In a real app, we would use fx_rate_to_base
        for (Account acc : accounts) {
            // This is a placeholder as Account entity doesn't store balance directly in this schema
            // Balance would be calculated from transactions. For now, we'll return zero or implement a simple sum.
        }

        for (Allocation alc : allocations) {
            totalAllocated = totalAllocated.add(alc.getAmount());
            wealthByCurrency.merge(alc.getCurrency().name(), alc.getAmount(), BigDecimal::add);
        }

        BigDecimal income = monthlyTransactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = monthlyTransactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardResponse.builder()
                .totalWealth(totalLiquid.add(totalAllocated))
                .totalLiquidAssets(totalLiquid)
                .totalAllocatedFunds(totalAllocated)
                .wealthByCurrency(wealthByCurrency)
                .monthlyIncome(income)
                .monthlyExpense(expense)
                .monthlySavings(income.subtract(expense))
                .build();
    }
}
