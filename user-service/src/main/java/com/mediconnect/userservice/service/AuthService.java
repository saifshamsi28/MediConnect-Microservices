package com.mediconnect.userservice.service;

import com.mediconnect.userservice.dto.DoctorRequest;
import com.mediconnect.userservice.dto.LoginRequest;
import com.mediconnect.userservice.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final KeycloakUserService keycloakUserService;
    private final WebClient webClient;

    @Value("${keycloak.serverUrl}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.clientId}")
    private String clientId;


    public void createDoctor(DoctorRequest request) {
        String kId=keycloakUserService.createUser(request.getUsername(),request.getPassword(),request.getEmail(),request.getFirstName(),request.getLastName(),request.getRole());
        log.info("kid: {}",kId);
    }


    public Mono<LoginResponse> login(LoginRequest request) {

        return webClient.post()
                .uri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", clientId)
                        .with("username", request.getUsername())
                        .with("password", request.getPassword())
                )
                .retrieve()
                .bodyToMono(Map.class)
                .map(token -> LoginResponse.builder()
                        .accessToken((String) token.get("access_token"))
                        .refreshToken((String) token.get("refresh_token"))
                        .expiresIn(((Number) token.get("expires_in")).longValue())
                        .tokenType((String) token.get("token_type"))
                        .build()
                );
    }
}