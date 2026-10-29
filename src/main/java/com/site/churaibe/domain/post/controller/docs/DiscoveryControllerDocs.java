package com.site.churaibe.domain.post.controller.docs;

import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Discovery", description = "홈 / 핵심 재료 기반 스펙트럼 탐색 API")
public interface DiscoveryControllerDocs {

    @Operation(summary = "홈 화면 조회", description = "로그인 없이 조회할 수 있습니다. 인기 Top3(조회수 기준), 최신 꿀조합 10개, 카테고리별 꿀조합 수를 한 번에 반환합니다.")
    ApiResponse<PostResDTO.HomeDTO> getHome();

    @Operation(summary = "핵심 재료 목록 조회", description = "꿀조합이 많이 등록된 핵심 재료를 많은 순으로 최대 20개 반환합니다. 탐색 페이지의 재료 선택에 사용합니다.")
    ApiResponse<PostResDTO.CoreIngredientListDTO> getCoreIngredients();

    @Operation(summary = "스펙트럼 탐색", description = "핵심 재료 하나를 기준으로 익숙한 조합 → 퓨전 → 신메뉴 → 실험적 조합 순서로 묶어 인기순 상위 꿀조합을 반환합니다. 스펙트럼이 지정되지 않은 기존 게시글은 포함되지 않습니다.")
    ApiResponse<PostResDTO.SpectrumExploreDTO> exploreSpectrum(@Parameter(description = "핵심 재료 (예: 계란)") String coreIngredient,
                                                               @Parameter(description = "단계별 최대 개수 (1~20)") int size);
}
