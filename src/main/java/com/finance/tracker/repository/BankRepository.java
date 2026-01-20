package com.finance.tracker.repository;

import com.finance.tracker.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Bank entity
 */
@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    
    Optional<Bank> findByCode(String code);
    
    Optional<Bank> findByName(String name);
}

