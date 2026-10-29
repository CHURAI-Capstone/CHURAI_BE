package com.site.churaibe.domain.comment.dto.request;

import com.site.churaibe.domain.comment.enums.CommentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentReqDTO {

    // parentId가 없으면 댓글, 있으면 해당 댓글에 대한 대댓글
    // type은 최상위 댓글에서만 쓰이고(없으면 GENERAL), 대댓글은 항상 GENERAL
    public record CommentSaveDTO(
            @NotBlank(message = "댓글 내용은 필수입니다.")
            @Size(max = 1000, message = "댓글은 1000자 이하여야 합니다.")
            String contents,

            Long parentId,

            CommentType type
    ) {
    }

    // 내용만 수정할 수 있고 type, 부모 댓글은 바꿀 수 없음
    public record CommentUpdateDTO(
            @NotBlank(message = "댓글 내용은 필수입니다.")
            @Size(max = 1000, message = "댓글은 1000자 이하여야 합니다.")
            String contents
    ) {
    }
}
