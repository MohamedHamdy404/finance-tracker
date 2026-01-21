package com.finance.tracker.mapper;

import com.finance.tracker.dto.allocation.AllocationCreateRequest;
import com.finance.tracker.dto.allocation.AllocationResponse;
import com.finance.tracker.entity.Allocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AllocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "allocationStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Allocation toEntity(AllocationCreateRequest request);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountName", expression = "java(allocation.getAccount() != null ? allocation.getAccount().getName() : null)")
    AllocationResponse toResponse(Allocation allocation);
}
