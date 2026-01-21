package com.finance.tracker.repository;

import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Transaction entity
 * Uses nested property syntax (user_Id, account_Id) for derived queries
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions for a user (with account and category loaded)
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    List<Transaction> findByUser_IdOrderByTransactionDateDesc(Long userId);

    /**
     * Find all transactions for a specific account
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    List<Transaction> findByAccount_IdOrderByTransactionDateDesc(Long accountId);

    /**
     * Find transaction by ID and user ID (authorization check)
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    @Query("SELECT t FROM Transaction t WHERE t.id = :transactionId AND t.user.id = :userId")
    Optional<Transaction> findByIdAndUser_Id(
            @Param("transactionId") Long transactionId,
            @Param("userId") Long userId);

    /**
     * Find transactions by transfer group ID (for linked transfers)
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    List<Transaction> findByTransferGroupId(UUID transferGroupId);

    /**
     * Find transactions by user and date range
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    List<Transaction> findByUser_IdAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Find transactions by user, account, and date range
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    List<Transaction> findByUser_IdAndAccount_IdAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long userId, Long accountId, LocalDate startDate, LocalDate endDate);

    /**
     * Find transactions by user and type (for reports - excludes TRANSFER)
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    List<Transaction> findByUser_IdAndTransactionTypeOrderByTransactionDateDesc(
            Long userId, TransactionType transactionType);

    /**
     * Find transactions by user, type, and date range (for reports)
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    List<Transaction> findByUser_IdAndTransactionTypeAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long userId, TransactionType transactionType, LocalDate startDate, LocalDate endDate);

    /**
     * Find transactions by user and category
     */
    @EntityGraph(attributePaths = {"account", "account.bank", "category"})
    List<Transaction> findByUser_IdAndCategory_IdOrderByTransactionDateDesc(
            Long userId, Long categoryId);

    /**
     * Check if transaction belongs to user
     */
    boolean existsByIdAndUser_Id(Long transactionId, Long userId);

    /**
     * Check if account belongs to user (for validation)
     */
    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.id = :accountId AND a.user.id = :userId")
    boolean accountBelongsToUser(@Param("accountId") Long accountId, @Param("userId") Long userId);
}
