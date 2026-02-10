package com.mediconnect.userservice.controller;

import com.mediconnect.userservice.dto.RegistrationRequest;
import com.mediconnect.userservice.dto.LoginRequest;
import com.mediconnect.userservice.dto.LoginResponse;
import com.mediconnect.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationRequest request){
        authService.create(request);
        return new ResponseEntity<>("Registered Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public Mono<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
