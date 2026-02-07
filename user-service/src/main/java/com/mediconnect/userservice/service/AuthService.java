package com.mediconnect.userservice.service;

import com.mediconnect.userservice.requestDto.LoginRequest;
import com.mediconnect.userservice.requestDto.UserRegisterRequest;
import com.mediconnect.userservice.responseDto.LoginResponse;

public interface AuthService {
    void registerUser(UserRegisterRequest request);
    LoginResponse login(LoginRequest request);
}
