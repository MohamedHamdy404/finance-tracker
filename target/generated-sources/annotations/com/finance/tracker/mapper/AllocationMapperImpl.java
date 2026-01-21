package com.finance.tracker.mapper;

import com.finance.tracker.dto.allocation.AllocationCreateRequest;
import com.finance.tracker.dto.allocation.AllocationResponse;
import com.finance.tracker.entity.Account;
import com.finance.tracker.entity.Allocation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T11:09:26-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Ubuntu)"
)
@Component
public class AllocationMapperImpl implements AllocationMapper {

    @Override
    public Allocation toEntity(AllocationCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Allocation.AllocationBuilder allocation = Allocation.builder();

        allocation.containerType( request.getContainerType() );
        allocation.name( request.getName() );
        allocation.amount( request.getAmount() );
        allocation.currency( request.getCurrency() );
        allocation.startDate( request.getStartDate() );
        allocation.maturityDate( request.getMaturityDate() );
        allocation.notes( request.getNotes() );

        return allocation.build();
    }

    @Override
    public AllocationResponse toResponse(Allocation allocation) {
        if ( allocation == null ) {
            return null;
        }

        AllocationResponse allocationResponse = new AllocationResponse();

        allocationResponse.setAccountId( allocationAccountId( allocation ) );
        allocationResponse.setId( allocation.getId() );
        allocationResponse.setContainerType( allocation.getContainerType() );
        allocationResponse.setName( allocation.getName() );
        allocationResponse.setAmount( allocation.getAmount() );
        allocationResponse.setCurrency( allocation.getCurrency() );
        allocationResponse.setStartDate( allocation.getStartDate() );
        allocationResponse.setMaturityDate( allocation.getMaturityDate() );
        allocationResponse.setAllocationStatus( allocation.getAllocationStatus() );
        allocationResponse.setNotes( allocation.getNotes() );

        allocationResponse.setAccountName( allocation.getAccount() != null ? allocation.getAccount().getName() : null );

        return allocationResponse;
    }

    private Long allocationAccountId(Allocation allocation) {
        if ( allocation == null ) {
            return null;
        }
        Account account = allocation.getAccount();
        if ( account == null ) {
            return null;
        }
        Long id = account.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
