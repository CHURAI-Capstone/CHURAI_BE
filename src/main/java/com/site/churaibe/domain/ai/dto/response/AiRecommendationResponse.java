package com.site.churaibe.domain.ai.dto.response;

import com.site.churaibe.domain.post.dto.response.PostResDTO;
import java.util.List;

public record AiRecommendationResponse(
        List<RecommendedPost> recommendations
) {

    public record RecommendedPost(
            PostResDTO.PostSummaryDTO post,
            String reason
    ) {
    }
}