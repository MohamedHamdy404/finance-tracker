package com.finance.tracker.mapper;

import com.finance.tracker.dto.transaction.TransactionCreateRequest;
import com.finance.tracker.dto.transaction.TransactionResponse;
import com.finance.tracker.dto.transaction.TransactionUpdateRequest;
import com.finance.tracker.entity.Transaction;
import org.mapstruct.*;

/**
 * MapStruct mapper for Transaction entity
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /**
     * Map create request to Transaction entity
     * User, Account, and Category will be set in service layer
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "transferDirection", ignore = true)
    @Mapping(target = "transferGroupId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Transaction toEntity(TransactionCreateRequest request);

    /**
     * Map Transaction entity to response DTO
     * Account and Category must be eagerly loaded via @EntityGraph
     */
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountName", expression = "java(buildAccountName(transaction))")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", expression = "java(buildCategoryName(transaction))")
    TransactionResponse toResponse(Transaction transaction);

    /**
     * Update existing transaction from update request
     * Only updates non-null fields (partial update)
     * Cannot update: transactionType, transferDirection, transferGroupId, user, account
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    @Mapping(target = "transferDirection", ignore = true)
    @Mapping(target = "transferGroupId", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Transaction transaction, TransactionUpdateRequest request);

    /**
     * Helper method to build account name with bank
     */
    default String buildAccountName(Transaction transaction) {
        if (transaction.getAccount() == null) {
            return null;
        }
        return transaction.getAccount().getBank().getName() + " - " +
               transaction.getAccount().getName();
    }

    /**
     * Helper method to build category name
     */
    default String buildCategoryName(Transaction transaction) {
        if (transaction.getCategory() == null) {
            return null;
        }
        return transaction.getCategory().getName();
    }
}
