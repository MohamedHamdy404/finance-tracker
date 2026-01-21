package com.finance.tracker.mapper;

import com.finance.tracker.dto.transaction.TransactionCreateRequest;
import com.finance.tracker.dto.transaction.TransactionResponse;
import com.finance.tracker.dto.transaction.TransactionUpdateRequest;
import com.finance.tracker.entity.Account;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.Transaction;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T11:09:26-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Ubuntu)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public Transaction toEntity(TransactionCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Transaction.TransactionBuilder transaction = Transaction.builder();

        transaction.transactionType( request.getTransactionType() );
        transaction.amount( request.getAmount() );
        transaction.currency( request.getCurrency() );
        transaction.transactionDate( request.getTransactionDate() );
        transaction.description( request.getDescription() );
        transaction.fxRateToBase( request.getFxRateToBase() );
        transaction.notes( request.getNotes() );

        return transaction.build();
    }

    @Override
    public TransactionResponse toResponse(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionResponse.TransactionResponseBuilder transactionResponse = TransactionResponse.builder();

        transactionResponse.accountId( transactionAccountId( transaction ) );
        transactionResponse.categoryId( transactionCategoryId( transaction ) );
        transactionResponse.id( transaction.getId() );
        transactionResponse.transactionType( transaction.getTransactionType() );
        transactionResponse.transferDirection( transaction.getTransferDirection() );
        transactionResponse.transferGroupId( transaction.getTransferGroupId() );
        transactionResponse.amount( transaction.getAmount() );
        transactionResponse.currency( transaction.getCurrency() );
        transactionResponse.transactionDate( transaction.getTransactionDate() );
        transactionResponse.description( transaction.getDescription() );
        transactionResponse.fxRateToBase( transaction.getFxRateToBase() );
        transactionResponse.notes( transaction.getNotes() );
        transactionResponse.createdAt( transaction.getCreatedAt() );
        transactionResponse.updatedAt( transaction.getUpdatedAt() );

        transactionResponse.accountName( buildAccountName(transaction) );
        transactionResponse.categoryName( buildCategoryName(transaction) );

        return transactionResponse.build();
    }

    @Override
    public void updateEntity(Transaction transaction, TransactionUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getAmount() != null ) {
            transaction.setAmount( request.getAmount() );
        }
        if ( request.getTransactionDate() != null ) {
            transaction.setTransactionDate( request.getTransactionDate() );
        }
        if ( request.getDescription() != null ) {
            transaction.setDescription( request.getDescription() );
        }
        if ( request.getFxRateToBase() != null ) {
            transaction.setFxRateToBase( request.getFxRateToBase() );
        }
        if ( request.getNotes() != null ) {
            transaction.setNotes( request.getNotes() );
        }
    }

    private Long transactionAccountId(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        Account account = transaction.getAccount();
        if ( account == null ) {
            return null;
        }
        Long id = account.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long transactionCategoryId(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        Category category = transaction.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
