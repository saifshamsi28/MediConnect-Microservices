package com.mediconnect.userservice.service;

import com.mediconnect.userservice.dto.DoctorRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final KeycloakUserService keycloakUserService;

    public void createDoctor(DoctorRequest request) {
        String kId=keycloakUserService.createUser(request.getUsername(),request.getPassword(),request.getEmail(),request.getFirstName(),request.getLastName(),request.getRole());
        log.info("kid: {}",kId);
    }
}