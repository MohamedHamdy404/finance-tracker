package com.finance.tracker.service;

import com.finance.tracker.dto.allocation.AllocationCreateRequest;
import com.finance.tracker.dto.allocation.AllocationResponse;
import com.finance.tracker.entity.Account;
import com.finance.tracker.entity.Allocation;
import com.finance.tracker.entity.User;
import com.finance.tracker.entity.enums.AllocationStatus;
import com.finance.tracker.entity.enums.ContainerType;
import com.finance.tracker.exception.InvalidRequestException;
import com.finance.tracker.exception.ResourceNotFoundException;
import com.finance.tracker.mapper.AllocationMapper;
import com.finance.tracker.repository.AllocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AllocationService {

    private final AllocationRepository allocationRepository;
    private final AllocationMapper allocationMapper;
    private final UserService userService;
    private final AccountService accountService;

    public AllocationResponse createAllocation(Long userId, AllocationCreateRequest request) {
        log.debug("Creating allocation for user: {}", userId);

        if (request.getContainerType() == ContainerType.ACCOUNT_BASED && request.getAccountId() == null) {
            throw new InvalidRequestException("Account ID is required for ACCOUNT_BASED allocations");
        }

        User user = userService.getUserEntityById(userId);
        Account account = null;
        if (request.getAccountId() != null) {
            account = accountService.getAccountEntityById(userId, request.getAccountId());
        }

        Allocation allocation = allocationMapper.toEntity(request);
        allocation.setUser(user);
        allocation.setAccount(account);
        allocation.setAllocationStatus(AllocationStatus.ACTIVE);

        Allocation saved = allocationRepository.save(allocation);
        return allocationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AllocationResponse> getUserAllocations(Long userId) {
        return allocationRepository.findByUser_Id(userId).stream()
                .map(allocationMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteAllocation(Long userId, Long allocationId) {
        Allocation allocation = allocationRepository.findByIdAndUser_Id(allocationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));
        allocationRepository.delete(allocation);
    }
}
