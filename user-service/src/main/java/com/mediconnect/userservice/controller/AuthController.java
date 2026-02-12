package com.mediconnect.userservice.controller;

import com.mediconnect.userservice.dto.RegistrationRequest;
import com.mediconnect.userservice.dto.LoginRequest;
import com.mediconnect.userservice.dto.LoginResponse;
import com.mediconnect.userservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Authentication Controller
 *
 * Handles user registration, login, token refresh, and logout.
 * Implements HTTP-only cookies for secure refresh token storage.
 *
 * Token Strategy:
 * - Access Token: Returned in response body (short-lived, typically 5-15 minutes)
 * - Refresh Token: Stored in HTTP-only secure cookie (long-lived, typically 7 days)
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user
     * @param request Registration details (username, password, email, etc.)
     * @return Success message with 201 Created status
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationRequest request) {
        authService.create(request);
        return new ResponseEntity<>("Registered Successfully", HttpStatus.CREATED);
    }

    /**
     * Login with username or email
     *
     * Access Token: Returned in response body
     * Refresh Token: Set as HTTP-only secure cookie (automatically by AuthService)
     *
     * @param request Login credentials (username/email and password)
     * @param response HttpServletResponse to set refresh token cookie
     * @return LoginResponse with access token
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        return authService.login(response, request)
                .map(loginResponse -> ResponseEntity.ok(loginResponse));
    }

    /**
     * Refresh access token using refresh token from cookie
     *
     * Extracts refresh token from HTTP-only cookie, validates it with Keycloak,
     * and returns a new access token. A new refresh token is also issued and
     * stored in the cookie.
     *
     * @param request HttpServletRequest to extract refresh token cookie
     * @param response HttpServletResponse to update refresh token cookie
     * @return LoginResponse with new access token
     */
    @PostMapping("/refresh")
    public Mono<ResponseEntity<LoginResponse>> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        // Extract refresh token from HTTP-only cookie
        String refreshToken = authService.extractRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            return Mono.error(new IllegalArgumentException("Refresh token not found in cookie"));
        }

        return authService.refreshToken(response, refreshToken)
                .map(loginResponse -> ResponseEntity.ok(loginResponse));
    }

    /**
     * Logout by clearing refresh token cookie
     *
     * @param response HttpServletResponse to clear refresh token cookie
     * @return Success message
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        authService.clearRefreshTokenCookie(response);
        return ResponseEntity.ok("Logged out successfully");
    }
}