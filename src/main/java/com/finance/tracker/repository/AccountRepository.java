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
    List<Account> findByUser_Id(Long userId);

    /**
     * Find all active accounts for a user (with bank eagerly loaded)
     */
    @EntityGraph(attributePaths = {"bank"})
    List<Account> findByUser_IdAndIsActiveTrue(Long userId);

    /**
     * Find accounts by user and currency (with bank eagerly loaded)
     */
    @EntityGraph(attributePaths = {"bank"})
    List<Account> findByUser_IdAndCurrency(Long userId, Currency currency);

    /**
     * Find account by ID and user ID (with bank eagerly loaded)
     */
    @EntityGraph(attributePaths = {"bank"})
    @Query("SELECT a FROM Account a WHERE a.id = :accountId AND a.user.id = :userId")
    Optional<Account> findByIdAndUser_Id(@Param("accountId") Long accountId,
                                         @Param("userId") Long userId);

    /**
     * Check if account belongs to user
     */
    boolean existsByIdAndUser_Id(Long accountId, Long userId);
}
