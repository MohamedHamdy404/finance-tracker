package com.finance.tracker.controller;

import com.finance.tracker.dto.account.AccountCreateRequest;
import com.finance.tracker.dto.account.AccountResponse;
import com.finance.tracker.dto.account.AccountUpdateRequest;
import com.finance.tracker.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Account CRUD operations
 * All endpoints are user-scoped via X-USER-ID header (temporary, will be replaced with JWT)
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    /**
     * Create a new account
     * POST /api/accounts
     * Header: X-USER-ID
     */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody AccountCreateRequest request) {
        
        log.debug("REST request to create account for user: {}", userId);
        AccountResponse response = accountService.createAccount(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all user accounts
     * GET /api/accounts
     * Header: X-USER-ID
     */
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts(
            @RequestHeader("X-USER-ID") Long userId) {
        
        log.debug("REST request to get all accounts for user: {}", userId);
        List<AccountResponse> accounts = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    /**
     * Get active accounts only
     * GET /api/accounts/active
     * Header: X-USER-ID
     */
    @GetMapping("/active")
    public ResponseEntity<List<AccountResponse>> getActiveAccounts(
            @RequestHeader("X-USER-ID") Long userId) {
        
        log.debug("REST request to get active accounts for user: {}", userId);
        List<AccountResponse> accounts = accountService.getActiveUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    /**
     * Get account by ID
     * GET /api/accounts/{id}
     * Header: X-USER-ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id) {
        
        log.debug("REST request to get account: id={}, userId={}", id, userId);
        AccountResponse account = accountService.getAccountById(userId, id);
        return ResponseEntity.ok(account);
    }

    /**
     * Update account
     * PUT /api/accounts/{id}
     * Header: X-USER-ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody AccountUpdateRequest request) {
        
        log.debug("REST request to update account: id={}, userId={}", id, userId);
        AccountResponse response = accountService.updateAccount(userId, id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete account (soft delete)
     * DELETE /api/accounts/{id}
     * Header: X-USER-ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id) {
        
        log.debug("REST request to delete account: id={}, userId={}", id, userId);
        accountService.deleteAccount(userId, id);
        return ResponseEntity.noContent().build();
    }
}