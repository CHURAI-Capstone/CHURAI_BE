package com.site.churaibe.domain.comment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentType {
    GENERAL("일반 댓글"),
    REVIEW("후기"),
    VARIATION("변형 조합"),
    ;

    private final String description;
}
