package com.mediconnect.userservice.dto;

import lombok.*;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private String tokenType;
}

