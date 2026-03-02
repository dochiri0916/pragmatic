package com.dochiri.pragmatic.presentation.user.response;

import com.dochiri.pragmatic.application.user.query.UserQueryService;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "활성 사용자 응답")
public record ActiveUserResponse(
        @Schema(description = "사용자 ID", example = "1")
        Long id,

        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "권한", example = "USER")
        String role
) {
    public static ActiveUserResponse of(UserQueryService.ActiveUserOutput output) {
        return new ActiveUserResponse(
                output.id(),
                output.email(),
                output.name(),
                output.role()
        );
    }
}
