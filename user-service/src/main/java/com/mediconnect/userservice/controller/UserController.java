package com.mediconnect.userservice.controller;

import com.mediconnect.userservice.requestDto.UserRegisterRequest;
import com.mediconnect.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterRequest request){
        userService.registerUser(request);
        return new ResponseEntity<>("User Registered Successfully", HttpStatus.CREATED);
    }
}
