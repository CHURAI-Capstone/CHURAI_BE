package com.site.churaibe.domain.scrap.dto.response;

import lombok.Builder;

public class ScrapResDTO {

    // scrapped는 비로그인이면 항상 false
    @Builder
    public record ScrapDTO(
            boolean scrapped,
            long scrapCount
    ) {
    }
}
