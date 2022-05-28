package com.blockwit.booking.configuration.initializer;

import com.blockwit.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDataInitializer {

    private final UserRepository userRepository;

}
