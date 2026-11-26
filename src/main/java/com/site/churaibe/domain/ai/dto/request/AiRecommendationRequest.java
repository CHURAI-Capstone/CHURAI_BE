package com.site.churaibe.domain.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AiRecommendationRequest(

        @NotBlank(message = "추천받고 싶은 내용을 입력해주세요.")
        @Size(
                max = 250,
                message = "추천 요청은 250자 이내여야 합니다."
        )
        String prompt

) {
}