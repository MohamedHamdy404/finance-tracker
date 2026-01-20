// ============================================

package com.finance.tracker.repository;

import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Category entity
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find all categories for a user
     */
    List<Category> findByUserId(Long userId);
    
    /**
     * Find all active categories for a user
     */
    List<Category> findByUserIdAndIsActiveTrue(Long userId);
    
    /**
     * Find categories by user and type
     */
    List<Category> findByUserIdAndType(Long userId, CategoryType type);
    
    /**
     * Find active categories by user and type
     */
    List<Category> findByUserIdAndTypeAndIsActiveTrue(Long userId, CategoryType type);
    
    /**
     * Find category by ID and user ID (for authorization check)
     */
    @Query("SELECT c FROM Category c WHERE c.id = :categoryId AND c.user.id = :userId")
    Optional<Category> findByIdAndUserId(@Param("categoryId") Long categoryId, @Param("userId") Long userId);
    
    /**
     * Check if category with same name and type exists for user
     */
    boolean existsByUserIdAndNameAndType(Long userId, String name, CategoryType type);
    
    /**
     * Check if category belongs to user
     */
    boolean existsByIdAndUserId(Long categoryId, Long userId);
}