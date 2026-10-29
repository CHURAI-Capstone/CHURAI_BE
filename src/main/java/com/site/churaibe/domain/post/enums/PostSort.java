package com.site.churaibe.domain.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
@AllArgsConstructor
public enum PostSort {
    LATEST(Sort.by(Sort.Direction.DESC, "createdAt", "id")),
    // 좋아요 기능이 없어 조회수를 인기 지표로 사용
    POPULAR(Sort.by(Sort.Direction.DESC, "views", "id")),
    ;

    private final Sort sort;
}
