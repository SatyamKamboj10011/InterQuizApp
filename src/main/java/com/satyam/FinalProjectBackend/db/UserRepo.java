package com.satyam.FinalProjectBackend.db;

import com.satyam.FinalProjectBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User>findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
}

