// ============================================

package com.finance.tracker.repository;

import com.finance.tracker.entity.Account;
import com.finance.tracker.entity.enums.Currency;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Account entity
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    /**
     * Find all accounts for a specific user (with bank eagerly loaded)
     */
    @EntityGraph(attributePaths = {"bank"})
    List<Account> findByUserId(Long userId);
    
    /**
     * Find all active accounts for a user (with bank eagerly loaded)
     */
    @EntityGraph(attributePaths = {"bank"})
    List<Account> findByUserIdAndIsActiveTrue(Long userId);
    
    /**
     * Find accounts by user and currency (with bank eagerly loaded)
     */
    @EntityGraph(attributePaths = {"bank"})
    List<Account> findByUserIdAndCurrency(Long userId, Currency currency);
    
    /**
     * Find account by ID and user ID (with bank eagerly loaded)
     */
    @EntityGraph(attributePaths = {"bank"})
    @Query("SELECT a FROM Account a WHERE a.id = :accountId AND a.user.id = :userId")
    Optional<Account> findByIdAndUserId(@Param("accountId") Long accountId, @Param("userId") Long userId);
    
    /**
     * Check if account belongs to user
     */
    boolean existsByIdAndUserId(Long accountId, Long userId);
}