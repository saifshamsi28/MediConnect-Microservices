package com.mediconnect.userservice.init;

import com.mediconnect.userservice.enums.Role;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeycloakRoleInitializer implements ApplicationRunner {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String REALM;

    @Override
    public void run(ApplicationArguments args) {

        List<String> existingRoles = keycloak.realm(REALM)
                .roles()
                .list()
                .stream()
                .map(RoleRepresentation::getName)
                .toList();

        for (Role role : Role.values()) {

            if (!existingRoles.contains(role.name())) {

                RoleRepresentation newRole = new RoleRepresentation();
                newRole.setName(role.name());

                keycloak.realm(REALM)
                        .roles()
                        .create(newRole);

                System.out.println(
                        "Created Keycloak role: " + role.name());
            }
        }
    }
}
