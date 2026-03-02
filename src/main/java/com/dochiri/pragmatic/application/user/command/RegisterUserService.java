package com.dochiri.pragmatic.application.user.command;

import com.dochiri.pragmatic.domain.user.User;
import com.dochiri.pragmatic.domain.user.UserRepository;
import com.dochiri.pragmatic.domain.common.exception.BaseException;
import com.dochiri.pragmatic.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Output execute(Input input) {
        checkDuplicateEmail(input.email());

        User user = userRepository.save(
                User.register(
                        input.email(),
                        passwordEncoder.encode(input.password()),
                        input.name()
                )
        );
        return Output.of(user);
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new BaseException(ErrorCode.DUPLICATE_EMAIL, "email", email);
        }
    }

    public record Input(
            String email,
            String password,
            String name
    ) {
        public Input {
            Objects.requireNonNull(email);
            Objects.requireNonNull(password);
            Objects.requireNonNull(name);
        }
    }

    public record Output(
            Long id,
            String email,
            String name,
            String role
    ) {
        public static Output of(User user) {
            return new Output(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole().name()
            );
        }
    }

}