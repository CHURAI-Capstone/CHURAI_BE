package com.site.churaibe.domain.post.enums;

import com.site.churaibe.domain.reaction.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 랭킹 정렬 기준, 동점이면 조회수 → 최신 순
@Getter
@AllArgsConstructor
public enum RankingCriteria {
    VIEWS("조회수 순", null),
    TRIED("먹어본 사람 순", ReactionType.TRIED),
    DELICIOUS("맛있었어요 순", ReactionType.DELICIOUS),
    UNIQUE_TASTE("맛이 특이해요 순", ReactionType.UNIQUE_TASTE),
    SCRAP("스크랩 순", null),
    ;

    private final String description;
    // 해당 반응 개수로 순위를 매기는 기준이면 그 반응, 아니면 null
    private final ReactionType reactionType;
}
