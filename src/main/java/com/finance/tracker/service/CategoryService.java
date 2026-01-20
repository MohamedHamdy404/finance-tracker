package com.finance.tracker.service;

import com.finance.tracker.dto.category.CategoryCreateRequest;
import com.finance.tracker.dto.category.CategoryResponse;
import com.finance.tracker.dto.category.CategoryUpdateRequest;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.User;
import com.finance.tracker.entity.enums.CategoryType;
import com.finance.tracker.exception.DuplicateResourceException;
import com.finance.tracker.exception.InvalidRequestException;
import com.finance.tracker.exception.ResourceNotFoundException;
import com.finance.tracker.mapper.CategoryMapper;
import com.finance.tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Category CRUD operations
 * All operations are user-scoped via authenticated userId
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserService userService;

    /**
     * Create a new category for the authenticated user
     */
    public CategoryResponse createCategory(Long userId, CategoryCreateRequest request) {
        log.debug("Creating category for user: {}, name: {}, type: {}", userId, request.getName(), request.getType());

        // Check for duplicate
        if (categoryRepository.existsByUserIdAndNameAndType(userId, request.getName(), request.getType())) {
            throw new DuplicateResourceException(
                    "Category with name '" + request.getName() + "' and type '" + request.getType() + "' already exists");
        }

        // Get authenticated user
        User user = userService.getUserEntityById(userId);

        // Map to entity
        Category category = categoryMapper.toEntity(request);
        category.setUser(user);

        // Save
        Category savedCategory = categoryRepository.save(category);
        log.info("Category created: id={}, userId={}, name={}", savedCategory.getId(), userId, savedCategory.getName());

        return categoryMapper.toResponse(savedCategory);
    }

    /**
     * Get all categories for authenticated user
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getUserCategories(Long userId) {
        log.debug("Fetching categories for user: {}", userId);
        return categoryRepository.findByUserId(userId).stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get active categories only
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveUserCategories(Long userId) {
        log.debug("Fetching active categories for user: {}", userId);
        return categoryRepository.findByUserIdAndIsActiveTrue(userId).stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get categories by type (INCOME or EXPENSE)
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesByType(Long userId, CategoryType type) {
        log.debug("Fetching categories for user: {}, type: {}", userId, type);
        return categoryRepository.findByUserIdAndTypeAndIsActiveTrue(userId, type).stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get category by ID (with user authorization check)
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long userId, Long categoryId) {
        log.debug("Fetching category: id={}, userId={}", categoryId, userId);
        Category category = findCategoryByIdAndUserId(categoryId, userId);
        return categoryMapper.toResponse(category);
    }

    /**
     * Update category (partial update)
     */
    public CategoryResponse updateCategory(Long userId, Long categoryId, CategoryUpdateRequest request) {
        log.debug("Updating category: id={}, userId={}", categoryId, userId);

        // Validate at least one field provided
        if (request.getName() == null && request.getIcon() == null && request.getColor() == null) {
            throw new InvalidRequestException("At least one field must be provided for update");
        }

        // Get existing category with authorization check
        Category category = findCategoryByIdAndUserId(categoryId, userId);

        // Check for duplicate name if name is being updated
        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByUserIdAndNameAndType(userId, request.getName(), category.getType())) {
                throw new DuplicateResourceException(
                        "Category with name '" + request.getName() + "' and type '" + category.getType() + "' already exists");
            }
        }

        // Update fields
        categoryMapper.updateEntity(category, request);

        // Save (no explicit save needed in transactional context)
        log.info("Category updated: id={}, userId={}", categoryId, userId);

        return categoryMapper.toResponse(category);
    }

    /**
     * Soft delete category (set isActive = false)
     */
    public void deleteCategory(Long userId, Long categoryId) {
        log.debug("Deleting category: id={}, userId={}", categoryId, userId);

        Category category = findCategoryByIdAndUserId(categoryId, userId);
        category.setIsActive(false);

        log.info("Category soft deleted: id={}, userId={}", categoryId, userId);
    }

    /**
     * Internal helper to find category with authorization check
     */
    private Category findCategoryByIdAndUserId(Long categoryId, Long userId) {
        return categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId + " for user: " + userId));
    }

    /**
     * Internal method to get category entity (used by other services)
     */
    public Category getCategoryEntityById(Long userId, Long categoryId) {
        return findCategoryByIdAndUserId(categoryId, userId);
    }
}