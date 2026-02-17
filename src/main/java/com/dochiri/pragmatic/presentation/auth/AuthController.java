package com.dochiri.pragmatic.presentation.auth;

import com.dochiri.pragmatic.application.auth.dto.LoginResult;
import com.dochiri.pragmatic.application.auth.command.RevokeTokenService;
import com.dochiri.pragmatic.application.auth.facade.LoginFacade;
import com.dochiri.pragmatic.application.auth.facade.ReissueTokenFacade;
import com.dochiri.pragmatic.infrastructure.security.cookie.CookieProvider;
import com.dochiri.pragmatic.presentation.auth.request.LoginRequest;
import com.dochiri.pragmatic.presentation.auth.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginFacade loginFacade;
    private final CookieProvider cookieProvider;
    private final ReissueTokenFacade reissueTokenFacade;
    private final RevokeTokenService revokeTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResult loginResult = loginFacade.login(request.toCommand());

        cookieProvider.addRefreshToken(response, loginResult.refreshToken());

        return ResponseEntity.ok(
                AuthResponse.from(loginResult.user(), loginResult.accessToken())
        );
    }

    @PostMapping("/reissue")
    public ResponseEntity<AuthResponse> reissue(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        LoginResult loginResult = reissueTokenFacade.reissue(refreshToken);

        cookieProvider.addRefreshToken(response, loginResult.refreshToken());

        return ResponseEntity.ok(
                AuthResponse.from(loginResult.user(), loginResult.accessToken())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (StringUtils.hasText(refreshToken)) {
            revokeTokenService.revokeByToken(refreshToken);
        }

        cookieProvider.deleteRefreshToken(response);
        return ResponseEntity.noContent().build();
    }

}
