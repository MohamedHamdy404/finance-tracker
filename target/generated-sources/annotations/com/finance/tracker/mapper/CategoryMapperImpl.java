package com.finance.tracker.mapper;

import com.finance.tracker.dto.category.CategoryCreateRequest;
import com.finance.tracker.dto.category.CategoryResponse;
import com.finance.tracker.dto.category.CategoryUpdateRequest;
import com.finance.tracker.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-20T23:57:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CategoryCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.name( request.getName() );
        category.type( request.getType() );
        category.icon( request.getIcon() );
        category.color( request.getColor() );

        category.isActive( true );

        return category.build();
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.id( category.getId() );
        categoryResponse.name( category.getName() );
        categoryResponse.type( category.getType() );
        categoryResponse.icon( category.getIcon() );
        categoryResponse.color( category.getColor() );
        categoryResponse.isActive( category.getIsActive() );
        categoryResponse.createdAt( category.getCreatedAt() );
        categoryResponse.updatedAt( category.getUpdatedAt() );

        categoryResponse.displayName( buildDisplayName(category) );

        return categoryResponse.build();
    }

    @Override
    public void updateEntity(Category category, CategoryUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            category.setName( request.getName() );
        }
        if ( request.getIcon() != null ) {
            category.setIcon( request.getIcon() );
        }
        if ( request.getColor() != null ) {
            category.setColor( request.getColor() );
        }
    }
}
