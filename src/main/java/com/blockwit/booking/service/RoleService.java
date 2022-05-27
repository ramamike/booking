package com.blockwit.booking.service;

import com.blockwit.booking.entity.Role;
import com.blockwit.booking.exceptions.RoleNotFoundException;
import com.blockwit.booking.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    public Role getRole(String role) {
        return roleRepository.findByRole(role).get();
    }

}
