package com.dochiri.pragmatic.application.auth.usecase;

import com.dochiri.pragmatic.application.auth.command.IssueRefreshTokenService;
import com.dochiri.pragmatic.application.auth.command.UserAuthenticationService;
import com.dochiri.pragmatic.domain.user.User;
import com.dochiri.pragmatic.infrastructure.security.jwt.JwtTokenGenerator;
import com.dochiri.pragmatic.infrastructure.security.jwt.JwtTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserAuthenticationService userAuthenticationService;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final IssueRefreshTokenService issueRefreshTokenService;

    @Transactional
    public Output execute(Input input) {
        User user = userAuthenticationService.execute(
                new UserAuthenticationService.Input(input.email(), input.password())
        );

        user.updateLastLoginAt();

        JwtTokenResult tokenResult = jwtTokenGenerator.generateToken(user.getId(), user.getRole().name());

        issueRefreshTokenService.execute(
                new IssueRefreshTokenService.Input(
                        tokenResult.refreshToken(),
                        user.getId(),
                        tokenResult.refreshTokenExpiresAt()
                )
        );

        return new Output(user, tokenResult.accessToken(), tokenResult.refreshToken());
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

    public record Output(
            User user,
            String accessToken,
            String refreshToken
    ) {
    }

}