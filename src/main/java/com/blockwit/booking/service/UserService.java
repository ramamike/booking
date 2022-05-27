package com.blockwit.booking.service;

import com.blockwit.booking.entity.User;
import com.blockwit.booking.exceptions.RoleNotFoundException;
import com.blockwit.booking.model.Error;
import com.blockwit.booking.model.Status;
import com.blockwit.booking.repository.UserRepository;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;

    private RoleService roleService;

    public Either<Error, User> createAccount(String inLogin,
                                             String inEmail,
                                             String password) {
        String login = inLogin.trim().toLowerCase();
        String email = inEmail.trim().toLowerCase();

        if (userRepository.existsByUserName(login)) {
            log.trace("Login: " + login + " already exists!");
            return Either.left(new Error(Error.EC_LOGIN_EXISTS, Error.EM_LOGIN_EXISTS + login));
        }

        if (userRepository.existsByEmail(email)) {
            log.trace("Email: " + email + " already exists!");
            return Either.left(new Error(Error.EC_EMAIL_EXISTS, Error.EM_EMAIL_EXISTS + email));
        }
        return Either.right(userRepository.save(User.builder()
                .userName(login)
                .email(email)
                .roles(Set.of(roleService.getRole("CLIENT")))
                .status(Status.ACTIVE)
                .password(password)
                .build()));
    }

}
