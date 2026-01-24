package com.example.pragmatic.infrastructure.persistence;

import com.example.pragmatic.domain.user.User;
import com.example.pragmatic.domain.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndStatus(Long id, UserStatus status);

    Optional<User> findByEmailAndStatus(String email, UserStatus status);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}