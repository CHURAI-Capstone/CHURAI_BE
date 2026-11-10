package com.site.churaibe.domain.post.controller.docs;

import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.RankingCriteria;
import com.site.churaibe.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ranking", description = "인기 랭킹 API")
public interface RankingControllerDocs {

    @Operation(summary = "인기 랭킹 조회", description = "로그인 없이 조회할 수 있습니다. 기준(조회수/먹어본 사람/맛있었어요/맛이 특이해요/스크랩) 내림차순이며 동점이면 조회수, 최신 순입니다. 각 항목에 반응별 개수, 스크랩 수, 댓글 수가 함께 담깁니다. page는 0부터 시작하고 size는 1~50입니다.")
    ApiResponse<PostResDTO.RankingPageDTO> getRankings(@Parameter(description = "랭킹 기준 (기본 VIEWS)") RankingCriteria criteria,
                                                       @Parameter(description = "카테고리 필터 (선택)") Category category,
                                                       @Parameter(description = "페이지 (0부터)") int page,
                                                       @Parameter(description = "페이지 크기 (1~50)") int size);
}
