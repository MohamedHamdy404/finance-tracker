package com.finance.tracker.controller;

import com.finance.tracker.dto.bank.BankResponse;
import com.finance.tracker.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Bank operations (read-only reference data)
 * No authentication required - public reference data
 */
@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
@Slf4j
public class BankController {

    private final BankService bankService;

    /**
     * Get all available banks
     * GET /api/banks
     */
    @GetMapping
    public ResponseEntity<List<BankResponse>> getAllBanks() {
        log.debug("REST request to get all banks");
        List<BankResponse> banks = bankService.getAllBanks();
        return ResponseEntity.ok(banks);
    }

    /**
     * Get bank by ID
     * GET /api/banks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankResponse> getBankById(@PathVariable Long id) {
        log.debug("REST request to get bank by id: {}", id);
        BankResponse bank = bankService.getBankById(id);
        return ResponseEntity.ok(bank);
    }
}