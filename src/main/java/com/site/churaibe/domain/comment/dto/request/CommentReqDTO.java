package com.site.churaibe.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentReqDTO {

    // parentId가 없으면 댓글, 있으면 해당 댓글에 대한 대댓글
    public record CommentSaveDTO(
            @NotBlank(message = "댓글 내용은 필수입니다.")
            @Size(max = 1000, message = "댓글은 1000자 이하여야 합니다.")
            String contents,

            Long parentId
    ) {
    }
}
