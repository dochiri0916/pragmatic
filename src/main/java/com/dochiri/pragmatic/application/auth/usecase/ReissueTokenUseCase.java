package com.dochiri.pragmatic.application.auth.usecase;

import com.dochiri.pragmatic.application.auth.command.IssueRefreshTokenService;
import com.dochiri.pragmatic.application.auth.command.VerifyRefreshTokenService;
import com.dochiri.pragmatic.application.user.query.UserLoader;
import com.dochiri.pragmatic.domain.auth.RefreshToken;
import com.dochiri.pragmatic.domain.user.User;
import com.dochiri.pragmatic.infrastructure.security.jwt.JwtTokenGenerator;
import com.dochiri.pragmatic.infrastructure.security.jwt.JwtTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class ReissueTokenUseCase {

    private final VerifyRefreshTokenService verifyRefreshTokenService;
    private final UserLoader userLoader;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Transactional
    public Output execute(Input input) {
        VerifyRefreshTokenService.Output verified = verifyRefreshTokenService.execute(
                new VerifyRefreshTokenService.Input(input.refreshToken())
        );

        User user = userLoader.loadByIdAndDeletedAtIsNull(verified.userId());

        JwtTokenResult tokenResult = jwtTokenGenerator.generateToken(user.getId(), user.getRole().name());

        verified.refreshToken().rotate(tokenResult.refreshToken(), tokenResult.refreshTokenExpiresAt());

        return new Output(user, tokenResult.accessToken(), tokenResult.refreshToken());
    }

    public record Input(
            String refreshToken
    ) {
        public Input {
            requireNonNull(refreshToken);
        }
    }

    public record Output(
            User user,
            String accessToken,
            String refreshToken
    ) {
    }

}