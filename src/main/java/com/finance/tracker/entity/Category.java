package com.finance.tracker.entity;

import com.finance.tracker.entity.enums.CategoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Category entity - User-defined transaction categories
 * Used to classify income and expenses for reporting
 */
@Entity
@Table(
    name = "categories",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_category_name_type",
            columnNames = {"user_id", "name", "type"}
        )
    },
    indexes = {
        @Index(name = "idx_categories_user_id", columnList = "user_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Category name is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Category type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CategoryType type;

    @Size(max = 50)
    @Column(length = 50)
    private String icon;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color (e.g., #FF5733)")
    @Size(max = 7)
    @Column(length = 7)
    private String color;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}