package com.finance.tracker.service;

import com.finance.tracker.dto.bank.BankResponse;
import com.finance.tracker.entity.Bank;
import com.finance.tracker.exception.ResourceNotFoundException;
import com.finance.tracker.mapper.BankMapper;
import com.finance.tracker.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Bank operations (read-only)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    /**
     * Get all banks
     */
    public List<BankResponse> getAllBanks() {
        log.debug("Fetching all banks");
        return bankRepository.findAll().stream()
                .map(bankMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get bank by ID
     */
    public BankResponse getBankById(Long id) {
        log.debug("Fetching bank with id: {}", id);
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found with id: " + id));
        return bankMapper.toResponse(bank);
    }

    /**
     * Internal method to get bank entity (used by other services)
     */
    public Bank getBankEntityById(Long id) {
        return bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found with id: " + id));
    }
}