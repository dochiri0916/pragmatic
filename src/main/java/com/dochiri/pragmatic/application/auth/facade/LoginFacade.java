package com.dochiri.pragmatic.application.auth.facade;

import com.dochiri.pragmatic.application.auth.command.IssueRefreshTokenCommand;
import com.dochiri.pragmatic.application.auth.command.IssueRefreshTokenService;
import com.dochiri.pragmatic.application.auth.command.LoginCommand;
import com.dochiri.pragmatic.application.auth.command.UserAuthenticationService;
import com.dochiri.pragmatic.application.auth.dto.LoginResult;
import com.dochiri.pragmatic.domain.user.User;
import com.dochiri.pragmatic.infrastructure.security.jwt.JwtTokenGenerator;
import com.dochiri.pragmatic.infrastructure.security.jwt.JwtTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoginFacade {

    private final UserAuthenticationService userAuthenticationService;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final IssueRefreshTokenService issueRefreshTokenService;

    @Transactional
    public LoginResult login(LoginCommand command) {
        User user = userAuthenticationService.execute(command);

        user.updateLastLoginAt();

        JwtTokenResult tokenResult = jwtTokenGenerator.generateToken(user.getId(), user.getRole().name());

        issueRefreshTokenService.execute(
                new IssueRefreshTokenCommand(
                        tokenResult.refreshToken(),
                        user.getId(),
                        tokenResult.refreshTokenExpiresAt()
                )
        );

        return LoginResult.from(user, tokenResult.accessToken(), tokenResult.refreshToken());
    }

}