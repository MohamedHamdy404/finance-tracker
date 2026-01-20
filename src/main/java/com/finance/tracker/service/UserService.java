package com.finance.tracker.service;

import com.finance.tracker.entity.User;
import com.finance.tracker.exception.ResourceNotFoundException;
import com.finance.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for User operations
 * Full implementation will be provided with Authentication module
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * Internal method to get user entity by ID
     * Used by other services
     */
    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}