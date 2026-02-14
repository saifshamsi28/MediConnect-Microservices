package com.mediconnect.userservice.dto;

import lombok.*;

/**
 * Login Response DTO
 * 
 * Note: refreshToken is NOT included in the response body.
 * It is sent via HTTP-only cookie for security.
 */
@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private long expiresIn;
    private String tokenType;
}

