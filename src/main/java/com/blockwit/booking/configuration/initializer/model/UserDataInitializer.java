package com.blockwit.booking.configuration.initializer.model;

import com.blockwit.booking.entity.User;
import com.blockwit.booking.model.Status;
import com.blockwit.booking.repository.UserRepository;
import com.blockwit.booking.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class UserDataInitializer {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void initAdmin() {

        try {
            if (userRepository.getUserByUsername("admin").isEmpty()) {
                userRepository.save(User.builder()
                        .userName("admin")
                        .email("default@mail.com")
                        .roles(Set.of(roleService.getRole("ADMIN")))
                        .status(Status.ACTIVE)
                        .hashPassword(passwordEncoder.encode("password"))
                        .build());
                log.info("initialising: default admin creation");
            }
        } catch (Exception e) {
            log.info("initialising error for default admin", e);
        }
    }

}
