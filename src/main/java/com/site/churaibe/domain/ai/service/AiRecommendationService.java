package com.site.churaibe.domain.ai.service;

import com.site.churaibe.domain.ai.client.GeminiClient;
import com.site.churaibe.domain.ai.dto.response.AiRecommendationResponse;
import com.site.churaibe.domain.ai.dto.response.AiRecommendationResponse.RecommendedPost;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private static final int MAX_RECOMMENDATIONS = 3;
    private static final int MAX_REASON_LENGTH = 160;

    private final AiCandidateService aiCandidateService;
    private final GeminiClient geminiClient;

    public AiRecommendationResponse recommend(
            String prompt
    ) {

        // 1. DB에서 실제 게시글 후보 조회
        List<AiCandidateService.Candidate> candidates =
                aiCandidateService.loadCandidates();

        // 게시글이 없으면 빈 추천 결과 반환
        if (candidates.isEmpty()) {
            return new AiRecommendationResponse(
                    List.of()
            );
        }

        // 2. 실제 후보 게시글 ID를 Map에 저장
        Map<Long, AiCandidateService.Candidate> candidatesById =
                new LinkedHashMap<>();

        for (
                AiCandidateService.Candidate candidate
                : candidates
        ) {
            candidatesById.put(
                    candidate.post().postId(),
                    candidate
            );
        }

        // 3. Gemini 결과에서 실제 게시글만 선별
        Map<Long, RecommendedPost> recommendations =
                new LinkedHashMap<>();

        List<GeminiClient.Selection> selections =
                geminiClient.selectPosts(
                        prompt,
                        candidates
                );

        for (GeminiClient.Selection selection : selections) {

            AiCandidateService.Candidate candidate =
                    candidatesById.get(
                            selection.postId()
                    );

            // 잘못된 게시글 ID 또는 추천 이유 제외
            if (
                    candidate == null
                            || selection.reason() == null
                            || selection.reason().isBlank()
            ) {
                continue;
            }

            String reason =
                    selection.reason().strip();

            // 지나치게 긴 추천 이유 제한
            if (
                    reason.length()
                            > MAX_REASON_LENGTH
            ) {
                reason = reason.substring(
                        0,
                        MAX_REASON_LENGTH
                );
            }

            // 동일 게시글은 한 번만 추천
            recommendations.putIfAbsent(
                    selection.postId(),
                    new RecommendedPost(
                            candidate.post(),
                            reason
                    )
            );

            if (
                    recommendations.size()
                            >= MAX_RECOMMENDATIONS
            ) {
                break;
            }
        }

        // 4. 추천 결과 반환
        return new AiRecommendationResponse(
                List.copyOf(
                        recommendations.values()
                )
        );
    }
}