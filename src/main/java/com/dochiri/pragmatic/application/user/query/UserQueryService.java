package com.dochiri.pragmatic.application.user.query;

import com.dochiri.pragmatic.domain.common.exception.BaseException;
import com.dochiri.pragmatic.domain.common.exception.ErrorCode;
import com.dochiri.pragmatic.domain.user.User;
import com.dochiri.pragmatic.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService implements UserLoader {

    private final UserRepository userRepository;

    @Override
    public User loadByIdAndDeletedAtIsNull(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "id", id));
    }

    @Override
    public User loadByEmailAndDeletedAtIsNull(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "email", email));
    }

    public ActiveUserOutput getActiveUser(Long id) {
        User user = loadByIdAndDeletedAtIsNull(id);
        return ActiveUserOutput.of(user);
    }

    public record ActiveUserOutput(
            Long id,
            String email,
            String name,
            String role
    ) {
        public static ActiveUserOutput of(User user) {
            return new ActiveUserOutput(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole().name()
            );
        }
    }

}