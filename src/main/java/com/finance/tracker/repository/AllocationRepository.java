package com.finance.tracker.repository;

import com.finance.tracker.entity.Allocation;
import com.finance.tracker.entity.enums.AllocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    List<Allocation> findByUser_Id(Long userId);
    List<Allocation> findByUser_IdAndAllocationStatus(Long userId, AllocationStatus status);
    Optional<Allocation> findByIdAndUser_Id(Long id, Long userId);
}
