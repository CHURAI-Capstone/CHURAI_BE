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

    public record AvailabilityResDTO(
            boolean available
    ) {
    }

    // 마이페이지 상단에 보여줄 활동 요약
    @Builder
    public record ActivityCountDTO(
            // 내가 작성한 꿀조합
            long postCount,
            // 스크랩한 꿀조합
            long scrapCount,
            // 먹어봤어요를 누른 꿀조합
            long triedCount,
            // 맛 평가(맛있었어요/맛이 특이해요/내 취향은 아니에요)를 남긴 꿀조합
            long tasteReactionCount,
            // 내가 쓴 댓글·후기·변형 조합 (대댓글 포함)
            long commentCount
    ) {
    }

    @Builder
    public record MyInfoResDTO(
            Long userId,
            String email,
            String nickname,
            LocalDateTime createdAt,
            ActivityCountDTO activity
    ) {
    }

    @Builder
    public record ProfileUpdateResDTO(
            Long userId,
            String nickname,
            LocalDateTime updatedAt
    ) {
    }
}
