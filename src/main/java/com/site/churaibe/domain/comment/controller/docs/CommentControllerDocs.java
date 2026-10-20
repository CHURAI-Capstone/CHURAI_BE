package com.site.churaibe.domain.comment.controller.docs;

import com.site.churaibe.domain.comment.dto.request.CommentReqDTO;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment", description = "댓글 / 대댓글 작성 / 삭제 API")
public interface CommentControllerDocs {

    @Operation(summary = "댓글 / 대댓글 작성", description = "로그인이 필요합니다. parentId 없이 보내면 댓글, 같은 게시글의 최상위 댓글 id를 parentId로 보내면 대댓글입니다(대댓글에는 다시 달 수 없음). 댓글 목록은 게시글 상세 조회 응답의 comments에 트리 구조로 담깁니다.")
    ApiResponse<CommentResDTO.CommentSaveResDTO> createComment(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                               Long postId,
                                                               CommentReqDTO.CommentSaveDTO dto);

    @Operation(summary = "댓글 / 대댓글 삭제", description = "작성자 본인만 삭제할 수 있습니다(아니면 403). 최상위 댓글을 삭제하면 달린 대댓글도 함께 삭제됩니다.")
    ApiResponse<Void> deleteComment(@Parameter(hidden = true) CustomUserDetails userDetails,
                                    Long postId,
                                    Long commentId);
}
