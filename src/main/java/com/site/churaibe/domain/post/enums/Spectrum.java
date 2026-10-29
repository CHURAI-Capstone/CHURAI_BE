package com.site.churaibe.domain.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 익숙한 조합에서 실험적 조합까지, 탐색 페이지의 스펙트럼 순서와 같다
@Getter
@AllArgsConstructor
public enum Spectrum {
    FAMILIAR("익숙한 조합"),
    FUSION("퓨전"),
    NEW_MENU("신메뉴"),
    EXPERIMENTAL("실험적 조합"),
    ;

    private final String description;
}
