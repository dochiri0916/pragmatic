package com.dochiri.pragmatic.infrastructure.persistence.user;

import com.dochiri.pragmatic.domain.user.User;
import com.dochiri.pragmatic.domain.user.UserNotFoundException;
import com.dochiri.pragmatic.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public User findByIdAndDeletedAtIsNull(Long id) {
        return userJpaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> UserNotFoundException.withId(id));
    }

    @Override
    public User findByEmailAndDeletedAtIsNull(String email) {
        return userJpaRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> UserNotFoundException.withEmail(email));
    }

    @Override
    public boolean existsByEmailAndDeletedAtIsNull(String email) {
        return userJpaRepository.existsByEmailAndDeletedAtIsNull(email);
    }

}