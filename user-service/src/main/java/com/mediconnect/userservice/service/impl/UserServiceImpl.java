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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserAlreadyExistException("Username Already Exists");
        }
        User user=User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();

        userRepository.save(user);
    }
}
