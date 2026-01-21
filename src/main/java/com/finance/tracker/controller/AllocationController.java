package com.finance.tracker.controller;

import com.finance.tracker.dto.allocation.AllocationCreateRequest;
import com.finance.tracker.dto.allocation.AllocationResponse;
import com.finance.tracker.entity.User;
import com.finance.tracker.service.AllocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;

    @PostMapping
    public ResponseEntity<AllocationResponse> createAllocation(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AllocationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(allocationService.createAllocation(user.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<AllocationResponse>> getUserAllocations(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(allocationService.getUserAllocations(user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllocation(@AuthenticationPrincipal User user, @PathVariable Long id) {
        allocationService.deleteAllocation(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
