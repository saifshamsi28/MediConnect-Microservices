package com.mediconnect.userservice.service;

import com.mediconnect.userservice.dto.RegistrationRequest;
import com.mediconnect.userservice.dto.LoginRequest;
import com.mediconnect.userservice.dto.LoginResponse;
import com.mediconnect.userservice.entity.User;
import com.mediconnect.userservice.exception.UserAlreadyExistException;
import com.mediconnect.userservice.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;
    private final WebClient webClient;

    @Value("${keycloak.serverUrl}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.clientId}")
    private String clientId;


    public void create(RegistrationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserAlreadyExistException("Username already exits");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistException("Email already exits");
        }

        String kId=keycloakUserService.createUser(request.getUsername(),request.getPassword(),request.getEmail(),request.getFirstName(),request.getLastName(),request.getRole());
        log.info("kid: {}",kId);
        try {

            User user = User.builder()
                    .kid(kId)
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .role(request.getRole())
                    .build();
            User savedUser=userRepository.save(user);
        }catch (Exception e){
            log.error("DB save failed, rolling back Keycloak user {}", kId);
            keycloakUserService.deleteUserById(kId);
            throw e;
        }
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