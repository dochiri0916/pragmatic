package com.dochiri.pragmatic.domain.user;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    boolean existsByEmailAndDeletedAtIsNull(String email);

}