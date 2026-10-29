package com.site.churaibe.domain.reaction.enums;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

// TRIED는 다른 반응과 함께 선택할 수 있고, 맛 평가 3종은 사용자당 게시글마다 하나만 선택됨
@Getter
@AllArgsConstructor
public enum ReactionType {
    TRIED("먹어봤어요", false),
    DELICIOUS("맛있었어요", true),
    UNIQUE_TASTE("맛이 특이해요", true),
    NOT_MY_TASTE("내 취향은 아니에요", true),
    ;

    private final String description;
    private final boolean taste;

    // 맛 평가를 바꿀 때 지워야 하는, 자기 자신을 뺀 나머지 맛 평가
    public List<ReactionType> otherTastes() {
        return Arrays.stream(values()).filter(type -> type.taste && type != this).toList();
    }
}
