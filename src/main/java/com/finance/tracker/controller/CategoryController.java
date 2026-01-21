package com.finance.tracker.controller;

import com.finance.tracker.dto.category.CategoryCreateRequest;
import com.finance.tracker.dto.category.CategoryResponse;
import com.finance.tracker.dto.category.CategoryUpdateRequest;
import com.finance.tracker.entity.User;
import com.finance.tracker.entity.enums.CategoryType;
import com.finance.tracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category CRUD operations
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Create a new category
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CategoryCreateRequest request) {
        
        log.debug("REST request to create category for user: {}", user.getId());
        CategoryResponse response = categoryService.createCategory(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all user categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getUserCategories(
            @AuthenticationPrincipal User user) {
        
        log.debug("REST request to get all categories for user: {}", user.getId());
        List<CategoryResponse> categories = categoryService.getUserCategories(user.getId());
        return ResponseEntity.ok(categories);
    }

    /**
     * Get active categories only
     */
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories(
            @AuthenticationPrincipal User user) {
        
        log.debug("REST request to get active categories for user: {}", user.getId());
        List<CategoryResponse> categories = categoryService.getActiveUserCategories(user.getId());
        return ResponseEntity.ok(categories);
    }

    /**
     * Get categories by type (INCOME or EXPENSE)
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByType(
            @AuthenticationPrincipal User user,
            @PathVariable CategoryType type) {
        
        log.debug("REST request to get categories by type: type={}, userId={}", type, user.getId());
        List<CategoryResponse> categories = categoryService.getCategoriesByType(user.getId(), type);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get category by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        
        log.debug("REST request to get category: id={}, userId={}", id, user.getId());
        CategoryResponse category = categoryService.getCategoryById(user.getId(), id);
        return ResponseEntity.ok(category);
    }

    /**
     * Update category
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        
        log.debug("REST request to update category: id={}, userId={}", id, user.getId());
        CategoryResponse response = categoryService.updateCategory(user.getId(), id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete category (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        
        log.debug("REST request to delete category: id={}, userId={}", id, user.getId());
        categoryService.deleteCategory(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}