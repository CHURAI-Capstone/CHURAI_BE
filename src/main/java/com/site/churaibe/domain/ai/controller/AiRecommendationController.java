package com.site.churaibe.domain.ai.controller;

import com.site.churaibe.domain.ai.dto.request.AiRecommendationRequest;
import com.site.churaibe.domain.ai.dto.response.AiRecommendationResponse;
import com.site.churaibe.domain.ai.service.AiRecommendationService;

import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.apiPayload.code.GeneralSuccessCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @PostMapping("/recommendations")
    public ApiResponse<AiRecommendationResponse> recommend(
            @RequestBody @Valid AiRecommendationRequest request
    ) {
        return ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                aiRecommendationService.recommend(
                        request.prompt()
                )
        );
    }
}