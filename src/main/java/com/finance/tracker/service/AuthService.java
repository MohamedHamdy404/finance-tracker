package com.finance.tracker.service;

import com.finance.tracker.dto.user.JwtResponse;
import com.finance.tracker.dto.user.LoginRequest;
import com.finance.tracker.dto.user.UserRegistrationRequest;
import com.finance.tracker.dto.user.UserResponse;
import com.finance.tracker.entity.User;
import com.finance.tracker.exception.DuplicateResourceException;
import com.finance.tracker.mapper.UserMapper;
import com.finance.tracker.repository.UserRepository;
import com.finance.tracker.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = (User) authentication.getPrincipal();

        return JwtResponse.builder()
                .accessToken(jwt)
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new DuplicateResourceException("Error: Email is already in use!");
        }

        User user = userMapper.toEntity(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());

        return userMapper.toResponse(savedUser);
    }
}
