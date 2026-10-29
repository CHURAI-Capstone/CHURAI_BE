package com.site.churaibe.domain.reaction.controller.docs;

import com.site.churaibe.domain.reaction.dto.request.ReactionReqDTO;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reaction", description = "먹어봤어요 / 맛있었어요 / 맛이 특이해요 / 내 취향은 아니에요 반응 API")
public interface ReactionControllerDocs {

    @Operation(summary = "반응 집계 조회", description = "로그인 없이 조회할 수 있습니다. counts에 반응 종류별 개수가 항상 모두 담기고, 로그인한 경우 myReactions에 내가 선택한 반응이 담깁니다(비로그인이면 빈 목록).")
    ApiResponse<ReactionResDTO.ReactionSummaryDTO> getReactions(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                               Long postId);

    @Operation(summary = "반응 선택 / 변경", description = "로그인이 필요합니다. type은 TRIED(먹어봤어요), DELICIOUS(맛있었어요), UNIQUE_TASTE(맛이 특이해요), NOT_MY_TASTE(내 취향은 아니에요)입니다. 맛 평가 3종은 게시글마다 하나만 유지되어, 다른 맛 평가를 보내면 기존 맛 평가가 새 것으로 바뀝니다. TRIED는 맛 평가와 함께 선택할 수 있습니다. 이미 선택한 반응을 다시 보내도 에러 없이 현재 상태를 반환합니다.")
    ApiResponse<ReactionResDTO.ReactionSummaryDTO> addReaction(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                              Long postId,
                                                              ReactionReqDTO.ReactionDTO dto);

    @Operation(summary = "반응 취소", description = "로그인이 필요합니다. 선택하지 않은 반응을 취소해도 에러 없이 현재 상태를 반환합니다.")
    ApiResponse<ReactionResDTO.ReactionSummaryDTO> cancelReaction(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                                 Long postId,
                                                                 @Parameter(description = "취소할 반응 종류") ReactionType type);
}
