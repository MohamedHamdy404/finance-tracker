package com.finance.tracker.mapper;

import com.finance.tracker.dto.bank.BankResponse;
import com.finance.tracker.entity.Bank;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for Bank entity
 */
@Mapper(componentModel = "spring")
public interface BankMapper {

    /**
     * Map Bank entity to response DTO
     */
    BankResponse toResponse(Bank bank);
}