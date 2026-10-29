package com.site.churaibe.domain.comment.dto.response;

import com.site.churaibe.domain.comment.enums.CommentType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class CommentResDTO {

    @Builder
    public record CommentSaveResDTO(
            Long commentId
    ) {
    }

    // replies는 최상위 댓글에만 담기고, 대댓글은 항상 빈 목록
    @Builder
    public record CommentDTO(
            Long commentId,
            CommentType type,
            String contents,
            Long writerId,
            String writerNickname,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<CommentDTO> replies
    ) {
    }
}
