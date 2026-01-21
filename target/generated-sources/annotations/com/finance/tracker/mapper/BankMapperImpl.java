package com.finance.tracker.mapper;

import com.finance.tracker.dto.bank.BankResponse;
import com.finance.tracker.entity.Bank;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-20T23:57:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class BankMapperImpl implements BankMapper {

    @Override
    public BankResponse toResponse(Bank bank) {
        if ( bank == null ) {
            return null;
        }

        BankResponse.BankResponseBuilder bankResponse = BankResponse.builder();

        bankResponse.id( bank.getId() );
        bankResponse.name( bank.getName() );
        bankResponse.code( bank.getCode() );
        bankResponse.country( bank.getCountry() );
        bankResponse.createdAt( bank.getCreatedAt() );

        return bankResponse.build();
    }
}
