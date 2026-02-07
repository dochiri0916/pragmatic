package com.example.pragmatic.application.user.query;

import com.example.pragmatic.domain.user.User;
import com.example.pragmatic.domain.user.UserNotFoundException;
import com.example.pragmatic.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService implements UserFinder {

    private final UserRepository userRepository;

    @Override
    public User findActiveUserById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다: " + id));
    }

    @Override
    public User findActiveUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다: " + email));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다: " + email));
    }

    public User getActiveUser(Long id) {
        return findActiveUserById(id);
    }

}