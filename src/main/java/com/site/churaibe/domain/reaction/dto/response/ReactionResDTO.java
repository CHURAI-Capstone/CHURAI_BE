package com.site.churaibe.domain.reaction.dto.response;

import com.site.churaibe.domain.reaction.enums.ReactionType;
import java.util.List;
import java.util.Map;
import lombok.Builder;

public class ReactionResDTO {

    // counts에는 모든 반응 종류가 0건이어도 항상 담기고, myReactions는 비로그인이면 빈 목록
    @Builder
    public record ReactionSummaryDTO(
            Map<ReactionType, Long> counts,
            List<ReactionType> myReactions
    ) {
    }
}
