package com.blockwit.booking.service;

import com.blockwit.booking.entity.Role;
import com.blockwit.booking.entity.User;
import com.blockwit.booking.exceptions.RoleNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.model.Error;
import com.blockwit.booking.model.Status;
import com.blockwit.booking.repository.UserRepository;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;

    private RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public Either<Error, User> createAccount(String inLogin,
                                             String inEmail,
                                             String password,
                                             boolean serviceProvider) throws RoleNotFoundException {

        String login = inLogin.trim().toLowerCase();
        String email = inEmail.trim().toLowerCase();

        if (userRepository.existsByUsername(login)) {
            log.trace("Login: " + login + " already exists!");
            return Either.left(new Error(Error.LOGIN_EXISTS + login));
        }

        if (userRepository.existsByEmail(email)) {
            log.trace("Email: " + email + " already exists!");
            return Either.left(new Error(Error.EMAIL_EXISTS + email));
        }

        Set<Role> rolesForAccount;
        if (serviceProvider) {
            rolesForAccount = Set.of(roleService.getRole("PROVIDER").orElseThrow(()-> new RoleNotFoundException()));
        } else {
            rolesForAccount = Set.of(roleService.getRole("CLIENT").orElseThrow(()-> new RoleNotFoundException()));
        }

        return Either.right(userRepository.save(User.builder()
                .username(login)
                .email(email)
                .roles(rolesForAccount)
                .status(Status.ACTIVE)
                .hashPassword(passwordEncoder.encode(password))
                .build()));
    }

    public Optional<User> getUserByUsername(String userName) {
        return userRepository.getUserByUsername(userName);
    }
}
