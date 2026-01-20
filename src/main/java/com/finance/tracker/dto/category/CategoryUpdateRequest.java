package com.finance.tracker.dto.category;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating category details (partial updates allowed)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateRequest {
    
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    private String name;
    
    @Size(max = 50, message = "Icon cannot exceed 50 characters")
    private String icon;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color (e.g., #FF5733)")
    @Size(max = 7)
    private String color;
}