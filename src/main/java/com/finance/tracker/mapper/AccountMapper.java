package com.finance.tracker.mapper;

import com.finance.tracker.dto.account.AccountCreateRequest;
import com.finance.tracker.dto.account.AccountResponse;
import com.finance.tracker.dto.account.AccountUpdateRequest;
import com.finance.tracker.entity.Account;
import org.mapstruct.*;

/**
 * MapStruct mapper for Account entity
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

    /**
     * Map create request to Account entity
     * User and Bank will be set in service layer
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // Set in service
    @Mapping(target = "bank", ignore = true) // Set in service
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(AccountCreateRequest request);

    /**
     * Map Account entity to response DTO
     * Bank must be eagerly loaded via @EntityGraph
     */
    @Mapping(target = "bankId", source = "bank.id")
    @Mapping(target = "bankName", source = "bank.name")
    @Mapping(target = "displayName", expression = "java(buildDisplayName(account))")
    AccountResponse toResponse(Account account);

    /**
     * Update existing account from update request
     * Only updates non-null fields (partial update)
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "bank", ignore = true)
    @Mapping(target = "accountType", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Account account, AccountUpdateRequest request);

    /**
     * Helper method to build display name
     */
    default String buildDisplayName(Account account) {
        return account.getBank().getName() + " - " + account.getName() + " (" + account.getCurrency() + ")";
    }
}