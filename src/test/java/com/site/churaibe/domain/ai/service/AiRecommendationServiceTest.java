package com.site.churaibe.domain.ai.service;
import com.site.churaibe.domain.ai.dto.response.AiRecommendationResponse;
import com.site.churaibe.domain.ai.client.GeminiClient;
import com.site.churaibe.domain.post.dto.response.PostResDTO;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiRecommendationServiceTest {

    @Mock
    AiCandidateService aiCandidateService;

    @Mock
    GeminiClient geminiClient;

    @InjectMocks
    AiRecommendationService service;

    @Test
    void excludesUnknownIdsAndDuplicates() {

        // 실제 DB 게시글이라고 가정
        PostResDTO.PostSummaryDTO post =
                PostResDTO.PostSummaryDTO.builder()
                        .postId(7L)
                        .title("치즈 토스트")
                        .build();

        List<AiCandidateService.Candidate> candidates =
                List.of(
                        new AiCandidateService.Candidate(
                                post,
                                List.of("치즈", "빵")
                        )
                );

        when(
                aiCandidateService.loadCandidates()
        ).thenReturn(candidates);

        // Gemini가 잘못된 ID와 중복 ID를 반환하는 상황
        when(
                geminiClient.selectPosts(
                        "간단한 간식",
                        candidates
                )
        ).thenReturn(
                List.of(
                        new GeminiClient.Selection(
                                999L,
                                "존재하지 않는 게시글"
                        ),
                        new GeminiClient.Selection(
                                7L,
                                "간단히 만들 수 있어요."
                        ),
                        new GeminiClient.Selection(
                                7L,
                                "중복 추천"
                        )
                )
        );

        AiRecommendationResponse result =
                service.recommend("간단한 간식");

        // 잘못된 ID와 중복 결과가 제외되어야 한다.
        assertThat(
                result.recommendations()
        ).hasSize(1);

        assertThat(
                result.recommendations()
                        .getFirst()
                        .post()
                        .postId()
        ).isEqualTo(7L);
    }
}