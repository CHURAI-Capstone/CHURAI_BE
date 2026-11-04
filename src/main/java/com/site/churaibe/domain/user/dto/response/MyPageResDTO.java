package com.site.churaibe.domain.user.dto.response;

import com.site.churaibe.domain.comment.enums.CommentType;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class MyPageResDTO {

    // 내가 남긴 반응과 그 꿀조합 (맛 평가 내역)
    @Builder
    public record ReactionHistoryDTO(
            ReactionType reactionType,
            String reactionDescription,
            LocalDateTime reactedAt,
            PostResDTO.PostSummaryDTO post
    ) {
    }

    @Builder
    public record ReactionHistoryPageDTO(
            List<ReactionHistoryDTO> reactions,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean hasNext
    ) {
    }

    // 내가 쓴 댓글·후기·변형 조합, 대댓글이면 parentId가 채워짐
    @Builder
    public record MyCommentDTO(
            Long commentId,
            CommentType type,
            String contents,
            Long parentId,
            Long postId,
            String postTitle,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
    }

    @Builder
    public record MyCommentPageDTO(
            List<MyCommentDTO> comments,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean hasNext
    ) {
    }
}
