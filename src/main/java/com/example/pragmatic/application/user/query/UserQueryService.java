package com.example.pragmatic.application.user.query;

import com.example.pragmatic.domain.user.User;
import com.example.pragmatic.domain.user.UserNotFoundException;
import com.example.pragmatic.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.pragmatic.domain.user.UserStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService implements UserFinder {

    private final UserRepository userRepository;

    @Override
    public User findActiveUserById(Long id) {
        return userRepository.findByIdAndStatus(id, ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다: " + id));
    }

    @Override
    public User findActiveUserByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다: " + email));
    }

    public User getActiveUser(Long id) {
        return findActiveUserById(id);
    }

}