package com.finance.tracker.mapper;

import com.finance.tracker.dto.category.CategoryCreateRequest;
import com.finance.tracker.dto.category.CategoryResponse;
import com.finance.tracker.dto.category.CategoryUpdateRequest;
import com.finance.tracker.entity.Category;
import org.mapstruct.*;

/**
 * MapStruct mapper for Category entity
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Map create request to Category entity
     * User will be set in service layer
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // Set in service
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryCreateRequest request);

    /**
     * Map Category entity to response DTO
     */
    @Mapping(target = "displayName", expression = "java(buildDisplayName(category))")
    CategoryResponse toResponse(Category category);

    /**
     * Update existing category from update request
     * Only updates non-null fields (partial update)
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Category category, CategoryUpdateRequest request);

    /**
     * Helper method to build display name
     */
    default String buildDisplayName(Category category) {
        if (category.getIcon() != null && !category.getIcon().isBlank()) {
            return category.getIcon() + " " + category.getName();
        }
        return category.getName();
    }
}