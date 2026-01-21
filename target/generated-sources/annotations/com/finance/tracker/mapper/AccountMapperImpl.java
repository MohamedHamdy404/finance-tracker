package com.finance.tracker.mapper;

import com.finance.tracker.dto.account.AccountCreateRequest;
import com.finance.tracker.dto.account.AccountResponse;
import com.finance.tracker.dto.account.AccountUpdateRequest;
import com.finance.tracker.entity.Account;
import com.finance.tracker.entity.Bank;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-20T23:57:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toEntity(AccountCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Account.AccountBuilder account = Account.builder();

        account.name( request.getName() );
        account.accountType( request.getAccountType() );
        account.currency( request.getCurrency() );

        account.isActive( true );

        return account.build();
    }

    @Override
    public AccountResponse toResponse(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountResponse.AccountResponseBuilder accountResponse = AccountResponse.builder();

        accountResponse.bankId( accountBankId( account ) );
        accountResponse.bankName( accountBankName( account ) );
        accountResponse.id( account.getId() );
        accountResponse.name( account.getName() );
        accountResponse.accountType( account.getAccountType() );
        accountResponse.currency( account.getCurrency() );
        accountResponse.isActive( account.getIsActive() );
        accountResponse.createdAt( account.getCreatedAt() );
        accountResponse.updatedAt( account.getUpdatedAt() );

        accountResponse.displayName( buildDisplayName(account) );

        return accountResponse.build();
    }

    @Override
    public void updateEntity(Account account, AccountUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            account.setName( request.getName() );
        }
    }

    private Long accountBankId(Account account) {
        if ( account == null ) {
            return null;
        }
        Bank bank = account.getBank();
        if ( bank == null ) {
            return null;
        }
        Long id = bank.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String accountBankName(Account account) {
        if ( account == null ) {
            return null;
        }
        Bank bank = account.getBank();
        if ( bank == null ) {
            return null;
        }
        String name = bank.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
