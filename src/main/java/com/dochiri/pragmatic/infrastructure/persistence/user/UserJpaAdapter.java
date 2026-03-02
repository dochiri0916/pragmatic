package com.dochiri.pragmatic.infrastructure.persistence.user;

import com.dochiri.pragmatic.domain.user.User;
import com.dochiri.pragmatic.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findByIdAndDeletedAtIsNull(Long id) {
        return userJpaRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Optional<User> findByEmailAndDeletedAtIsNull(String email) {
        return userJpaRepository.findByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public boolean existsByEmailAndDeletedAtIsNull(String email) {
        return userJpaRepository.existsByEmailAndDeletedAtIsNull(email);
    }

}