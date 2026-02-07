package com.mediconnect.userservice.service.impl;

import com.mediconnect.userservice.entity.User;
import com.mediconnect.userservice.exception.UserAlreadyExistException;
import com.mediconnect.userservice.repository.UserRepository;
import com.mediconnect.userservice.requestDto.LoginRequest;
import com.mediconnect.userservice.requestDto.UserRegisterRequest;
import com.mediconnect.userservice.responseDto.LoginResponse;
import com.mediconnect.userservice.security.JwtUtil;
import com.mediconnect.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
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

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();

        String token=jwtUtil.generateToken(userDetails);

        return new LoginResponse(token);
    }
}
