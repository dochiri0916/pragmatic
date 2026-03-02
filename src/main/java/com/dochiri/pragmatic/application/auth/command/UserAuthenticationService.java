package com.dochiri.pragmatic.application.auth.command;

import com.dochiri.pragmatic.application.user.query.UserLoader;
import com.dochiri.pragmatic.domain.common.exception.BaseException;
import com.dochiri.pragmatic.domain.common.exception.ErrorCode;
import com.dochiri.pragmatic.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserLoader userLoader;

    public User execute(Input input) {
        User user = userLoader.loadByEmailAndDeletedAtIsNull(input.email());

        validatePassword(input.password(), user.getPassword());

        return user;
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BaseException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public record Input(
            String email,
            String password
    ) {
        public Input {
            requireNonNull(email);
            requireNonNull(password);
        }
    }

}