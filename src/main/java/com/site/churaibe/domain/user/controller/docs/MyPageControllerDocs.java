package com.site.churaibe.domain.user.controller.docs;

import com.site.churaibe.domain.comment.enums.CommentType;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import com.site.churaibe.domain.user.dto.response.MyPageResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "MyPage", description = "마이페이지 개인 활동 내역 API (모두 로그인 필요)")
public interface MyPageControllerDocs {

    @Operation(summary = "내가 작성한 꿀조합", description = "최신순입니다. page는 0부터 시작, size는 1~50입니다.")
    ApiResponse<PostResDTO.PostPageDTO> getMyPosts(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                   @Parameter(description = "페이지 번호 (0부터)") int page,
                                                   @Parameter(description = "페이지 크기 (1~50)") int size);

    @Operation(summary = "스크랩 목록", description = "최근에 스크랩한 순입니다. page는 0부터 시작, size는 1~50입니다.")
    ApiResponse<PostResDTO.PostPageDTO> getMyScraps(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                    @Parameter(description = "페이지 번호 (0부터)") int page,
                                                    @Parameter(description = "페이지 크기 (1~50)") int size);

    @Operation(summary = "먹어본 꿀조합", description = "'먹어봤어요'를 누른 꿀조합을 최근 반응 순으로 보여줍니다. page는 0부터 시작, size는 1~50입니다.")
    ApiResponse<PostResDTO.PostPageDTO> getMyTriedPosts(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                        @Parameter(description = "페이지 번호 (0부터)") int page,
                                                        @Parameter(description = "페이지 크기 (1~50)") int size);

    @Operation(summary = "맛 평가 내역", description = "내가 남긴 맛 평가와 해당 꿀조합을 최근 순으로 보여줍니다. type을 비우면 DELICIOUS(맛있었어요)·UNIQUE_TASTE(맛이 특이해요)·NOT_MY_TASTE(내 취향은 아니에요) 전체이고, 지정하면 그 반응만 보여줍니다. page는 0부터 시작, size는 1~50입니다.")
    ApiResponse<MyPageResDTO.ReactionHistoryPageDTO> getMyReactions(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @Parameter(description = "반응 종류 필터 (선택)") ReactionType type,
            @Parameter(description = "페이지 번호 (0부터)") int page,
            @Parameter(description = "페이지 크기 (1~50)") int size);

    @Operation(summary = "댓글·후기 내역", description = "내가 쓴 댓글·후기·변형 조합(대댓글 포함)을 최근 순으로 보여줍니다. type(GENERAL | REVIEW | VARIATION)을 비우면 전체입니다. 대댓글이면 parentId가 채워지고, postId·postTitle로 해당 꿀조합으로 이동할 수 있습니다. page는 0부터 시작, size는 1~50입니다.")
    ApiResponse<MyPageResDTO.MyCommentPageDTO> getMyComments(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @Parameter(description = "댓글 종류 필터 (선택)") CommentType type,
            @Parameter(description = "페이지 번호 (0부터)") int page,
            @Parameter(description = "페이지 크기 (1~50)") int size);
}
