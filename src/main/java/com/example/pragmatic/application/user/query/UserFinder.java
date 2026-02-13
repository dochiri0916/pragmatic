package com.example.pragmatic.application.user.query;

import com.example.pragmatic.domain.user.User;
import java.util.Optional;

public interface UserFinder {

    Optional<User> findActiveUserById(Long id);

    Optional<User> findActiveUserByEmail(String email);

    Optional<User> findByEmail(String email);

}