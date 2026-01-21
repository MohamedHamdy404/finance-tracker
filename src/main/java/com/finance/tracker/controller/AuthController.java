package com.finance.tracker.controller;

import com.finance.tracker.dto.user.JwtResponse;
import com.finance.tracker.dto.user.LoginRequest;
import com.finance.tracker.dto.user.UserRegistrationRequest;
import com.finance.tracker.dto.user.UserResponse;
import com.finance.tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authService.registerUser(registrationRequest));
    }
}
