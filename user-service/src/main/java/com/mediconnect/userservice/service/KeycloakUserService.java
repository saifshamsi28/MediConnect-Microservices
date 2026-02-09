package com.mediconnect.userservice.service;

import com.mediconnect.userservice.enums.Role;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String REALM;

    @Transactional
    public String createUser(String username, String password, String email, String firstName, String lastName, Role role) {

        UserRepresentation user = new UserRepresentation();

        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        user.setEnabled(true);
        //user.setEmailVerified(true);
        user.setRequiredActions(List.of());


        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        Response response=keycloak.realm(REALM)
                .users()
                .create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException(
                    "Failed to create user in Keycloak. Status: " + response.getStatus());
        }

        String userId = response.getLocation()
                .getPath()
                .replaceAll(".*/([^/]+)$", "$1");

        String roleName = role.toString();

        var roleRepresentation = keycloak.realm(REALM)
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloak.realm(REALM)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(roleRepresentation));

        return userId;

    }

}
