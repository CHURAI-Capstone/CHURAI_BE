package com.site.churaibe.domain.user.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

public class UserResDTO {

    @Builder
    public record SignUpResDTO(
            Long userId
    ) {
    }

    @Builder
    public record LoginResDTO(
            Long userId,
            String accessToken
    ) {
    }

    @Builder
    public record MyInfoResDTO(
            Long userId,
            String email,
            String nickname,
            LocalDateTime createdAt
    ) {
    }
}
