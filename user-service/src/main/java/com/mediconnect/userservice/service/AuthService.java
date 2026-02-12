package com.mediconnect.userservice.service;

import com.mediconnect.userservice.client.DoctorClient;
import com.mediconnect.userservice.dto.DoctorRequest;
import com.mediconnect.userservice.dto.RegistrationRequest;
import com.mediconnect.userservice.dto.LoginRequest;
import com.mediconnect.userservice.dto.LoginResponse;
import com.mediconnect.userservice.entity.User;
import com.mediconnect.userservice.enums.Role;
import com.mediconnect.userservice.exception.UserAlreadyExistException;
import com.mediconnect.userservice.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Authentication Service
 *
 * Handles user authentication with Keycloak and supports flexible login
 * with both username and email.
 *
 * Token Management:
 * - Access Token: Short-lived (5-15 minutes), included in response body
 * - Refresh Token: Long-lived (7 days), stored in HTTP-only secure cookie
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;
    private final WebClient webClient;
    private final DoctorClient doctorClient;


    @Value("${keycloak.serverUrl}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${app.security.cookie.secure:false}")
    private boolean secureCookie;

    @Value("${app.security.cookie.domain:localhost}")
    private String cookieDomain;

    @Value("${app.security.cookie.sameSite:Strict}")
    private String sameSitePolicy;

    /**
     * Register a new user in both database and Keycloak
     *
     * @param request User registration details
     * @throws UserAlreadyExistException if username or email already exists
     */

    @Transactional
    public void create(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException("Email already exists");
        }

        // Create user in Keycloak
        String keycloakId = keycloakUserService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getRole()
        );
        log.info("User created in Keycloak with ID: {}", keycloakId);

        try {
            // Create user in database
            User user = User.builder()
                    .kid(keycloakId)
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .role(request.getRole())
                    .build();

            userRepository.save(user);

            if (request.getRole() == Role.DOCTOR) {

                DoctorRequest doctorRequest = new DoctorRequest();
                doctorRequest.setUserId(UUID.fromString(keycloakId));
                doctorRequest.setFirstName(request.getFirstName());
                doctorRequest.setLastName(request.getLastName());
                doctorRequest.setEmail(request.getEmail());
                doctorRequest.setActive(false);

                doctorClient.createDoctor(doctorRequest);
            }


            log.info("User saved to database: {}", request.getUsername());

        } catch (Exception e) {
            log.error("Database save failed, rolling back Keycloak user {}", keycloakId, e);
            keycloakUserService.deleteUserById(keycloakId);
            userRepository.deleteByKid(keycloakId);
            throw e;
        }
    }

    /**
     * Login with username or email
     *
     * Supports flexible login - users can provide either username or email.
     * If email is provided, the system looks up the username from the database.
     *
     * @param response HttpServletResponse to set refresh token cookie
     * @param request Login credentials (username/email and password)
     * @return Mono<LoginResponse> with access token (refresh token in cookie)
     */
    public Mono<LoginResponse> login(HttpServletResponse response, LoginRequest request) {
        String identifier = request.getUsername();
        String username;

        // Check if identifier is an email (contains @)
        if (identifier.contains("@")) {
            log.info("Login attempt with email: {}", identifier);

            // Lookup user by email to get username
            User user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> {
                        log.warn("Login failed: Email not found - {}", identifier);
                        return new IllegalArgumentException("Invalid credentials");
                    });
            username = user.getUsername();
            log.info("Email {} mapped to username {}", identifier, username);

        } else {
            // Use identifier as username directly
            username = identifier;
            log.info("Login attempt with username: {}", username);
        }

        // Authenticate with Keycloak using username
        return authenticateWithKeycloak(response, username, request.getPassword());
    }

    /**
     * Authenticate with Keycloak and get tokens
     *
     * Calls Keycloak's token endpoint with username/password credentials.
     * Sets the refresh token as an HTTP-only secure cookie.
     *
     * @param response HttpServletResponse to set refresh token cookie
     * @param username Username for authentication
     * @param password Password for authentication
     * @return Mono<LoginResponse> with access token
     */
    private Mono<LoginResponse> authenticateWithKeycloak(
            HttpServletResponse response,
            String username,
            String password) {

        return webClient.post()
                .uri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", clientId)
                        .with("username", username)
                        .with("password", password)
                )
                .retrieve()
                .bodyToMono(Map.class)
                .map(token -> {
                    // Extract tokens from Keycloak response
                    String accessToken = (String) token.get("access_token");
                    String refreshToken = (String) token.get("refresh_token");
                    long expiresIn = ((Number) token.get("expires_in")).longValue();
                    String tokenType = (String) token.get("token_type");

                    // Validate tokens
                    if (accessToken == null || refreshToken == null) {
                        throw new IllegalStateException("Keycloak response missing tokens");
                    }

                    // Set refresh token in HTTP-only secure cookie
                    setRefreshTokenCookie(response, refreshToken);
                    log.info("User {} logged in successfully, refresh token set in cookie", username);

                    // Return response with access token only
                    return LoginResponse.builder()
                            .accessToken(accessToken)
                            .expiresIn(expiresIn)
                            .tokenType(tokenType)
                            .build();
                })
                .doOnError(WebClientResponseException.class, e -> {
                    log.error("Keycloak authentication failed for username {}: {}",
                            username, e.getMessage());
                })
                .onErrorMap(WebClientResponseException.Unauthorized.class, e ->
                        new IllegalArgumentException("Invalid credentials")
                )
                .onErrorMap(WebClientResponseException.BadRequest.class, e ->
                        new IllegalArgumentException("Invalid authentication request")
                );
    }

    /**
     * Refresh the access token using a refresh token
     *
     * Calls Keycloak's token endpoint with the refresh token grant type.
     * Issues a new refresh token and sets it in the cookie.
     *
     * @param response HttpServletResponse to update refresh token cookie
     * @param refreshToken Current refresh token (from cookie)
     * @return Mono<LoginResponse> with new access token
     */
    public Mono<LoginResponse> refreshToken(HttpServletResponse response, String refreshToken) {
        return webClient.post()
                .uri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", clientId)
                        .with("refresh_token", refreshToken)
                )
                .retrieve()
                .bodyToMono(Map.class)
                .map(token -> {
                    // Extract tokens from Keycloak response
                    String accessToken = (String) token.get("access_token");
                    String newRefreshToken = (String) token.get("refresh_token");
                    long expiresIn = ((Number) token.get("expires_in")).longValue();
                    String tokenType = (String) token.get("token_type");

                    // Validate tokens
                    if (accessToken == null || newRefreshToken == null) {
                        throw new IllegalStateException("Keycloak response missing tokens");
                    }

                    // Set NEW refresh token in HTTP-only secure cookie
                    setRefreshTokenCookie(response, newRefreshToken);
                    log.info("Access token refreshed successfully, new refresh token set in cookie");

                    // Return response with new access token
                    return LoginResponse.builder()
                            .accessToken(accessToken)
                            .expiresIn(expiresIn)
                            .tokenType(tokenType)
                            .build();
                })
                .doOnError(WebClientResponseException.class, e -> {
                    log.error("Token refresh failed: {}", e.getMessage());
                })
                .onErrorMap(WebClientResponseException.Unauthorized.class, e ->
                        new IllegalArgumentException("Refresh token expired or invalid")
                )
                .onErrorMap(WebClientResponseException.BadRequest.class, e ->
                        new IllegalArgumentException("Invalid token refresh request")
                );
    }

    /**
     * Set refresh token as HTTP-only secure cookie
     *
     * Cookie settings:
     * - HttpOnly: Prevents JavaScript access (XSS protection)
     * - Secure: HTTPS only in production
     * - SameSite: Prevents CSRF attacks
     * - Path: Limited to auth endpoints
     * - MaxAge: 7 days (matches refresh token expiration)
     *
     * @param response HttpServletResponse
     * @param refreshToken Refresh token value
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        cookie.setHttpOnly(true);           // Prevents JavaScript access (XSS protection)
        cookie.setSecure(secureCookie);     // HTTPS only in production
        cookie.setPath("/api/v1/auth");     // Restrict to auth endpoints
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        // Set domain if configured
        if (cookieDomain != null && !cookieDomain.isEmpty() && !"localhost".equals(cookieDomain)) {
            cookie.setDomain(cookieDomain);
        }

        // SameSite attribute for CSRF protection
        // Note: Servlet API 4.0+ supports this via setAttribute
        response.addHeader("Set-Cookie",
                cookie.getName() + "=" + cookie.getValue()
                        + "; Path=" + cookie.getPath()
                        + "; Max-Age=" + cookie.getMaxAge()
                        + "; HttpOnly"
                        + (secureCookie ? "; Secure" : "")
                        + "; SameSite=" + sameSitePolicy);

        // Also add via standard cookie for older servlet versions
        response.addCookie(cookie);
    }

    /**
     * Extract refresh token from HTTP-only cookie
     *
     * @param request HttpServletRequest
     * @return Refresh token value or null if not found
     */
    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    /**
     * Clear refresh token cookie on logout
     *
     * Sets the cookie's max age to 0, causing the browser to delete it immediately.
     *
     * @param response HttpServletResponse
     */
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);

        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge(0); // Delete immediately

        // Set domain if configured
        if (cookieDomain != null && !cookieDomain.isEmpty() && !"localhost".equals(cookieDomain)) {
            cookie.setDomain(cookieDomain);
        }

        // Use Set-Cookie header for proper SameSite attribute
        response.addHeader("Set-Cookie",
                cookie.getName() + "=null"
                        + "; Path=" + cookie.getPath()
                        + "; Max-Age=" + cookie.getMaxAge()
                        + "; HttpOnly"
                        + (secureCookie ? "; Secure" : "")
                        + "; SameSite=" + sameSitePolicy);

        // Also add via standard cookie for older servlet versions
        response.addCookie(cookie);
    }
}