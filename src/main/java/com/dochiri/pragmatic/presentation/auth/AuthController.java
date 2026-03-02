package com.dochiri.pragmatic.presentation.auth;

import com.dochiri.pragmatic.application.auth.command.RevokeTokenService;
import com.dochiri.pragmatic.application.auth.usecase.LoginUseCase;
import com.dochiri.pragmatic.application.auth.usecase.ReissueTokenUseCase;
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

    private final LoginUseCase loginUseCase;
    private final CookieProvider cookieProvider;
    private final ReissueTokenUseCase reissueTokenUseCase;
    private final RevokeTokenService revokeTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginUseCase.Output output = loginUseCase.execute(request.toInput());

        cookieProvider.addRefreshToken(response, output.refreshToken());

        return ResponseEntity.ok(
                AuthResponse.from(output.user(), output.accessToken())
        );
    }

    @PostMapping("/reissue")
    public ResponseEntity<AuthResponse> reissue(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        ReissueTokenUseCase.Output output = reissueTokenUseCase.execute(
                new ReissueTokenUseCase.Input(refreshToken)
        );

        cookieProvider.addRefreshToken(response, output.refreshToken());

        return ResponseEntity.ok(
                AuthResponse.from(output.user(), output.accessToken())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (StringUtils.hasText(refreshToken)) {
            revokeTokenService.execute(new RevokeTokenService.Input(refreshToken));
        }

        cookieProvider.deleteRefreshToken(response);
        return ResponseEntity.noContent().build();
    }

}
