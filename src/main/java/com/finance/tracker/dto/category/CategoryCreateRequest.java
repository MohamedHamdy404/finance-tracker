package com.finance.tracker.dto.category;

import com.finance.tracker.entity.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new category
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateRequest {
    
    @NotBlank(message = "Category name is required")
    @Size(max = 100)
    private String name;
    
    @NotNull(message = "Category type is required")
    private CategoryType type;
    
    @Size(max = 50)
    private String icon;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color (e.g., #FF5733)")
    @Size(max = 7)
    private String color;
}