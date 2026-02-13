package com.example.pragmatic.presentation.user;

import com.example.pragmatic.application.user.command.RegisterService;
import com.example.pragmatic.application.user.query.UserQueryService;
import com.example.pragmatic.infrastructure.security.jwt.JwtPrincipal;
import com.example.pragmatic.presentation.user.request.RegisterRequest;
import com.example.pragmatic.presentation.user.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final RegisterService registerService;
    private final UserQueryService userQueryService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "등록 성공")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                UserResponse.from(registerService.register(request))
        );
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtPrincipal principal
    ) {
        return ResponseEntity.ok(
                UserResponse.from(userQueryService.getActiveUser(principal.userId()))
        );
    }

    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getActiveUser(@Parameter(description = "사용자 ID") @PathVariable Long id) {
        return ResponseEntity.ok(
                UserResponse.from(userQueryService.getActiveUser(id))
        );
    }

}