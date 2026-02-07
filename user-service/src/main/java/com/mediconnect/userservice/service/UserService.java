package com.mediconnect.userservice.service;

import com.mediconnect.userservice.requestDto.UserRegisterRequest;

public interface UserService {
    void registerUser(UserRegisterRequest request);
}
