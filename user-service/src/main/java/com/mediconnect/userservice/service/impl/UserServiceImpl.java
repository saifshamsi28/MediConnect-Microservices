package com.mediconnect.userservice.service.impl;

import com.mediconnect.userservice.entity.User;
import com.mediconnect.userservice.exception.UserAlreadyExistException;
import com.mediconnect.userservice.repository.UserRepository;
import com.mediconnect.userservice.requestDto.UserRegisterRequest;
import com.mediconnect.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

}
