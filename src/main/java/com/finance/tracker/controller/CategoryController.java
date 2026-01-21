package com.finance.tracker.controller;

import com.finance.tracker.dto.category.CategoryCreateRequest;
import com.finance.tracker.dto.category.CategoryResponse;
import com.finance.tracker.dto.category.CategoryUpdateRequest;
import com.finance.tracker.entity.enums.CategoryType;
import com.finance.tracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category CRUD operations
 * All endpoints are user-scoped via X-USER-ID header (temporary, will be replaced with JWT)
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Create a new category
     * POST /api/categories
     * Header: X-USER-ID
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody CategoryCreateRequest request) {
        
        log.debug("REST request to create category for user: {}", userId);
        CategoryResponse response = categoryService.createCategory(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all user categories
     * GET /api/categories
     * Header: X-USER-ID
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getUserCategories(
            @RequestHeader("X-USER-ID") Long userId) {
        
        log.debug("REST request to get all categories for user: {}", userId);
        List<CategoryResponse> categories = categoryService.getUserCategories(userId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get active categories only
     * GET /api/categories/active
     * Header: X-USER-ID
     */
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories(
            @RequestHeader("X-USER-ID") Long userId) {
        
        log.debug("REST request to get active categories for user: {}", userId);
        List<CategoryResponse> categories = categoryService.getActiveUserCategories(userId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get categories by type (INCOME or EXPENSE)
     * GET /api/categories/type/{type}
     * Header: X-USER-ID
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByType(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable CategoryType type) {
        
        log.debug("REST request to get categories by type: type={}, userId={}", type, userId);
        List<CategoryResponse> categories = categoryService.getCategoriesByType(userId, type);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get category by ID
     * GET /api/categories/{id}
     * Header: X-USER-ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id) {
        
        log.debug("REST request to get category: id={}, userId={}", id, userId);
        CategoryResponse category = categoryService.getCategoryById(userId, id);
        return ResponseEntity.ok(category);
    }

    /**
     * Update category
     * PUT /api/categories/{id}
     * Header: X-USER-ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        
        log.debug("REST request to update category: id={}, userId={}", id, userId);
        CategoryResponse response = categoryService.updateCategory(userId, id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete category (soft delete)
     * DELETE /api/categories/{id}
     * Header: X-USER-ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id) {
        
        log.debug("REST request to delete category: id={}, userId={}", id, userId);
        categoryService.deleteCategory(userId, id);
        return ResponseEntity.noContent().build();
    }
}