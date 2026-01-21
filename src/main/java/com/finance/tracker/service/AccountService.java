package com.finance.tracker.service;

import com.finance.tracker.dto.account.AccountCreateRequest;
import com.finance.tracker.dto.account.AccountResponse;
import com.finance.tracker.dto.account.AccountUpdateRequest;
import com.finance.tracker.entity.Account;
import com.finance.tracker.entity.Bank;
import com.finance.tracker.entity.User;
import com.finance.tracker.exception.InvalidRequestException;
import com.finance.tracker.exception.ResourceNotFoundException;
import com.finance.tracker.mapper.AccountMapper;
import com.finance.tracker.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Account CRUD operations
 * All operations are user-scoped via authenticated userId
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BankService bankService;
    private final UserService userService;

    /**
     * Create a new account for the authenticated user
     */
    public AccountResponse createAccount(Long userId, AccountCreateRequest request) {
        log.debug("Creating account for user: {}, bank: {}", userId, request.getBankId());

        // Get authenticated user
        User user = userService.getUserEntityById(userId);

        // Get bank (use default Bank Misr if not provided)
        Long bankId = request.getBankId() != null ? request.getBankId() : 1L;
        Bank bank = bankService.getBankEntityById(bankId);

        // Map to entity
        Account account = accountMapper.toEntity(request);
        account.setUser(user);
        account.setBank(bank);

        // Save
        Account savedAccount = accountRepository.save(account);
        log.info("Account created: id={}, userId={}, bankId={}", savedAccount.getId(), userId, bank.getId());

        return accountMapper.toResponse(savedAccount);
    }

    /**
     * Get all accounts for authenticated user
     */
    @Transactional(readOnly = true)
    public List<AccountResponse> getUserAccounts(Long userId) {
        log.debug("Fetching accounts for user: {}", userId);
        return accountRepository.findByUser_Id(userId).stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get active accounts only
     */
    @Transactional(readOnly = true)
    public List<AccountResponse> getActiveUserAccounts(Long userId) {
        log.debug("Fetching active accounts for user: {}", userId);
        return accountRepository.findByUser_IdAndIsActiveTrue(userId).stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get account by ID (with user authorization check)
     */
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long userId, Long accountId) {
        log.debug("Fetching account: id={}, userId={}", accountId, userId);
        Account account = findAccountByIdAndUserId(accountId, userId);
        return accountMapper.toResponse(account);
    }

    /**
     * Update account (partial update)
     */
    public AccountResponse updateAccount(Long userId, Long accountId, AccountUpdateRequest request) {
        log.debug("Updating account: id={}, userId={}", accountId, userId);

        // Validate at least one field provided
        if (request.getName() == null) {
            throw new InvalidRequestException("At least one field must be provided for update");
        }

        // Get existing account with authorization check
        Account account = findAccountByIdAndUserId(accountId, userId);

        // Update fields
        accountMapper.updateEntity(account, request);

        // Save (no explicit save needed in transactional context)
        log.info("Account updated: id={}, userId={}", accountId, userId);

        return accountMapper.toResponse(account);
    }

    /**
     * Soft delete account (set isActive = false)
     */
    public void deleteAccount(Long userId, Long accountId) {
        log.debug("Deleting account: id={}, userId={}", accountId, userId);

        Account account = findAccountByIdAndUserId(accountId, userId);
        account.setIsActive(false);

        log.info("Account soft deleted: id={}, userId={}", accountId, userId);
    }

    /**
     * Internal helper to find account with authorization check
     */
    private Account findAccountByIdAndUserId(Long accountId, Long userId) {
        return accountRepository.findByIdAndUser_Id(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with id: " + accountId + " for user: " + userId));
    }

    /**
     * Internal method to get account entity (used by other services)
     */
    public Account getAccountEntityById(Long userId, Long accountId) {
        return findAccountByIdAndUserId(accountId, userId);
    }
}