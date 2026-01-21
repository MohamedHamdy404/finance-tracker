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
    List<Category> findByUser_Id(Long userId);

    /**
     * Find all active categories for a user
     */
    List<Category> findByUser_IdAndIsActiveTrue(Long userId);

    /**
     * Find categories by user and type
     */
    List<Category> findByUser_IdAndType(Long userId, CategoryType type);

    /**
     * Find active categories by user and type
     */
    List<Category> findByUser_IdAndTypeAndIsActiveTrue(Long userId, CategoryType type);

    /**
     * Find category by ID and user ID (for authorization check)
     */
    @Query("SELECT c FROM Category c WHERE c.id = :categoryId AND c.user.id = :userId")
    Optional<Category> findByIdAndUser_Id(@Param("categoryId") Long categoryId,
                                          @Param("userId") Long userId);

    /**
     * Check if category with same name and type exists for user
     */
    boolean existsByUser_IdAndNameAndType(Long userId, String name, CategoryType type);

    /**
     * Check if category belongs to user
     */
    boolean existsByIdAndUser_Id(Long categoryId, Long userId);
}
