package com.finance.tracker.mapper;

import com.finance.tracker.dto.user.UserRegistrationRequest;
import com.finance.tracker.dto.user.UserResponse;
import com.finance.tracker.entity.User;
import org.mapstruct.*;

/**
 * MapStruct mapper for User entity
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Map registration request to User entity
     * Password will be encoded separately in the service layer
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Handled in service with BCrypt
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRegistrationRequest request);

    /**
     * Map User entity to response DTO
     */
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserResponse toResponse(User user);

    /**
     * Update existing user entity from update request
     * Ignores null fields (partial update)
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "baseCurrency", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget User user, com.finance.tracker.dto.user.UserUpdateRequest request);
}