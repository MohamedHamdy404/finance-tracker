package com.finance.tracker.controller;

import com.finance.tracker.dto.account.AccountCreateRequest;
import com.finance.tracker.dto.account.AccountResponse;
import com.finance.tracker.dto.account.AccountUpdateRequest;
import com.finance.tracker.entity.User;
import com.finance.tracker.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Account CRUD operations
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    /**
     * Create a new account
     */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AccountCreateRequest request) {
        
        log.debug("REST request to create account for user: {}", user.getId());
        AccountResponse response = accountService.createAccount(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all user accounts
     */
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts(
            @AuthenticationPrincipal User user) {
        
        log.debug("REST request to get all accounts for user: {}", user.getId());
        List<AccountResponse> accounts = accountService.getUserAccounts(user.getId());
        return ResponseEntity.ok(accounts);
    }

    /**
     * Get active accounts only
     */
    @GetMapping("/active")
    public ResponseEntity<List<AccountResponse>> getActiveAccounts(
            @AuthenticationPrincipal User user) {
        
        log.debug("REST request to get active accounts for user: {}", user.getId());
        List<AccountResponse> accounts = accountService.getActiveUserAccounts(user.getId());
        return ResponseEntity.ok(accounts);
    }

    /**
     * Get account by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        
        log.debug("REST request to get account: id={}, userId={}", id, user.getId());
        AccountResponse account = accountService.getAccountById(user.getId(), id);
        return ResponseEntity.ok(account);
    }

    /**
     * Update account
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody AccountUpdateRequest request) {
        
        log.debug("REST request to update account: id={}, userId={}", id, user.getId());
        AccountResponse response = accountService.updateAccount(user.getId(), id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete account (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        
        log.debug("REST request to delete account: id={}, userId={}", id, user.getId());
        accountService.deleteAccount(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}