package com.finance.tracker.controller;

import com.finance.tracker.dto.transaction.*;
import com.finance.tracker.entity.User;
import com.finance.tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Transaction operations
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Create a standard transaction (INCOME, EXPENSE, ADJUSTMENT)
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransactionCreateRequest request) {
        
        log.debug("REST request to create transaction for user: {}", user.getId());
        TransactionResponse response = transactionService.createTransaction(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Create a TRANSFER
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> createTransfer(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransferCreateRequest request) {
        
        log.debug("REST request to create transfer for user: {}", user.getId());
        TransferResponse response = transactionService.createTransfer(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all user transactions
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(
            @AuthenticationPrincipal User user) {
        
        log.debug("REST request to get all transactions for user: {}", user.getId());
        List<TransactionResponse> transactions = transactionService.getUserTransactions(user.getId());
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get transaction by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        
        log.debug("REST request to get transaction: id={}, userId={}", id, user.getId());
        TransactionResponse transaction = transactionService.getTransactionById(user.getId(), id);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Update transaction
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody TransactionUpdateRequest request) {
        
        log.debug("REST request to update transaction: id={}, userId={}", id, user.getId());
        TransactionResponse response = transactionService.updateTransaction(user.getId(), id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete transaction
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        
        log.debug("REST request to delete transaction: id={}, userId={}", id, user.getId());
        transactionService.deleteTransaction(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get transactions for a specific account
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(
            @AuthenticationPrincipal User user,
            @PathVariable Long accountId) {
        
        log.debug("REST request to get transactions for account: id={}, userId={}", accountId, user.getId());
        List<TransactionResponse> transactions = transactionService.getAccountTransactions(user.getId(), accountId);
        return ResponseEntity.ok(transactions);
    }
}
