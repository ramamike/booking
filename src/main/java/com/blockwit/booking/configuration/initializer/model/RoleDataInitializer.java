package com.blockwit.booking.configuration.initializer.model;

import com.blockwit.booking.entity.Role;
import com.blockwit.booking.repository.RoleRepository;
import com.blockwit.booking.repository.UserRepository;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class RoleDataInitializer {

    private final RoleRepository roleRepository;

    public void initRoles() {

        List<String> roleNames = Arrays.asList(
                "ADMIN",
                "CREATOR",
                "EDITOR",
                "CLIENT"
        );

        List<Role> roles = new ArrayList<>();

        roleNames.forEach(roleName -> {
            try {
                if (!roleRepository.existsByRole(roleName)) {
                    roles.add(Role.builder().role(roleName).build());
                    log.info("Role initialising for:" + roleName);
                }
            } catch (Exception e) {
                log.error("initialization: exception during a role searching by name", e);
            }
        });

        try {
            roleRepository.saveAll(roles);
        } catch (Exception e) {
            log.error("roles are not initialised");
        }
    }

}
