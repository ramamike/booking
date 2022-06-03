package com.blockwit.booking.repository;

import com.blockwit.booking.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> getUserByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
