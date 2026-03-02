package com.dochiri.pragmatic.presentation.user.response;

import com.dochiri.pragmatic.application.user.command.RegisterUserService;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 사용자 응답")
public record RegisterUserResponse(
        @Schema(description = "사용자 ID", example = "1")
        Long id,

        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "권한", example = "USER")
        String role
) {
    public static RegisterUserResponse of(RegisterUserService.Output output) {
        return new RegisterUserResponse(
                output.id(),
                output.email(),
                output.name(),
                output.role()
        );
    }
}