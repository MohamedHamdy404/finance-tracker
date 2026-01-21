package com.finance.tracker.service;

import com.finance.tracker.dto.transaction.*;
import com.finance.tracker.entity.Account;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.User;
import com.finance.tracker.entity.enums.TransactionType;
import com.finance.tracker.entity.enums.TransferDirection;
import com.finance.tracker.exception.InvalidRequestException;
import com.finance.tracker.exception.ResourceNotFoundException;
import com.finance.tracker.mapper.TransactionMapper;
import com.finance.tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for Transaction operations
 * Handles INCOME, EXPENSE, TRANSFER, and ADJUSTMENT
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final UserService userService;

    /**
     * Create a standard transaction (INCOME, EXPENSE, ADJUSTMENT)
     */
    public TransactionResponse createTransaction(Long userId, TransactionCreateRequest request) {
        log.debug("Creating transaction for user: {}, type: {}", userId, request.getTransactionType());

        if (request.getTransactionType() == TransactionType.TRANSFER) {
            throw new InvalidRequestException("Use /api/transactions/transfer for TRANSFER transactions");
        }

        User user = userService.getUserEntityById(userId);
        Account account = accountService.getAccountEntityById(userId, request.getAccountId());
        
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryService.getCategoryEntityById(userId, request.getCategoryId());
        }

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setCategory(category);

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction created: id={}, type={}", saved.getId(), saved.getTransactionType());

        return transactionMapper.toResponse(saved);
    }

    /**
     * Create a TRANSFER (creates two linked transactions)
     */
    public TransferResponse createTransfer(Long userId, TransferCreateRequest request) {
        log.debug("Creating transfer for user: {} from {} to {}", userId, request.getFromAccountId(), request.getToAccountId());

        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new InvalidRequestException("Source and destination accounts must be different");
        }

        User user = userService.getUserEntityById(userId);
        Account fromAccount = accountService.getAccountEntityById(userId, request.getFromAccountId());
        Account toAccount = accountService.getAccountEntityById(userId, request.getToAccountId());

        UUID transferGroupId = UUID.randomUUID();

        // 1. Create OUT transaction
        Transaction outTx = Transaction.builder()
                .user(user)
                .account(fromAccount)
                .transactionType(TransactionType.TRANSFER)
                .transferDirection(TransferDirection.OUT)
                .transferGroupId(transferGroupId)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .transactionDate(request.getTransferDate())
                .description(request.getDescription())
                .fxRateToBase(request.getFxRateToBase())
                .notes(request.getNotes())
                .build();

        // 2. Create IN transaction
        Transaction inTx = Transaction.builder()
                .user(user)
                .account(toAccount)
                .transactionType(TransactionType.TRANSFER)
                .transferDirection(TransferDirection.IN)
                .transferGroupId(transferGroupId)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .transactionDate(request.getTransferDate())
                .description(request.getDescription())
                .fxRateToBase(request.getFxRateToBase())
                .notes(request.getNotes())
                .build();

        Transaction savedOut = transactionRepository.save(outTx);
        Transaction savedIn = transactionRepository.save(inTx);

        log.info("Transfer created: groupId={}, outId={}, inId={}", transferGroupId, savedOut.getId(), savedIn.getId());

        return TransferResponse.builder()
                .transferGroupId(transferGroupId)
                .outgoingTransaction(transactionMapper.toResponse(savedOut))
                .incomingTransaction(transactionMapper.toResponse(savedIn))
                .build();
    }

    /**
     * Get all transactions for user
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getUserTransactions(Long userId) {
        return transactionRepository.findByUser_IdOrderByTransactionDateDesc(userId).stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get transaction by ID
     */
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findByIdAndUser_Id(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
        return transactionMapper.toResponse(transaction);
    }

    /**
     * Update transaction (partial)
     */
    public TransactionResponse updateTransaction(Long userId, Long transactionId, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findByIdAndUser_Id(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        if (transaction.isTransfer()) {
            throw new InvalidRequestException("Direct update of TRANSFER transactions is not allowed. Please delete and recreate.");
        }

        if (request.getCategoryId() != null) {
            Category category = categoryService.getCategoryEntityById(userId, request.getCategoryId());
            transaction.setCategory(category);
        }

        transactionMapper.updateEntity(transaction, request);
        log.info("Transaction updated: id={}", transactionId);

        return transactionMapper.toResponse(transaction);
    }

    /**
     * Delete transaction
     * If it's a transfer, deletes both linked transactions
     */
    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findByIdAndUser_Id(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        if (transaction.isTransfer()) {
            List<Transaction> linked = transactionRepository.findByTransferGroupId(transaction.getTransferGroupId());
            transactionRepository.deleteAll(linked);
            log.info("Transfer deleted: groupId={}, count={}", transaction.getTransferGroupId(), linked.size());
        } else {
            transactionRepository.delete(transaction);
            log.info("Transaction deleted: id={}", transactionId);
        }
    }

    /**
     * Get transactions for a specific account
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAccountTransactions(Long userId, Long accountId) {
        // Verify account ownership
        accountService.getAccountEntityById(userId, accountId);
        
        return transactionRepository.findByAccount_IdOrderByTransactionDateDesc(accountId).stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
