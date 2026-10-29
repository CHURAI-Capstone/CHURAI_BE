package com.site.churaibe.domain.scrap.controller.docs;

import com.site.churaibe.domain.scrap.dto.response.ScrapResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Scrap", description = "게시글 스크랩 API")
public interface ScrapControllerDocs {

    @Operation(summary = "스크랩", description = "로그인이 필요합니다. 이미 스크랩한 게시글이어도 에러 없이 현재 상태(scrapped, scrapCount)를 반환합니다.")
    ApiResponse<ScrapResDTO.ScrapDTO> addScrap(@Parameter(hidden = true) CustomUserDetails userDetails, Long postId);

    @Operation(summary = "스크랩 취소", description = "로그인이 필요합니다. 스크랩하지 않은 게시글이어도 에러 없이 현재 상태를 반환합니다.")
    ApiResponse<ScrapResDTO.ScrapDTO> cancelScrap(@Parameter(hidden = true) CustomUserDetails userDetails, Long postId);
}
