package com.blockwit.booking.service;

import com.blockwit.booking.entity.Role;
import com.blockwit.booking.exceptions.RoleNotFoundException;
import com.blockwit.booking.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    @Transactional
    public Optional<Role> getRole(String role) {
        return roleRepository.findByRole(role);
    }

}
