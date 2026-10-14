package com.site.churaibe.domain.post.dto.response;

import com.site.churaibe.domain.post.enums.Category;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class PostResDTO {

    @Builder
    public record PostSaveResDTO(
            Long postId
    ) {
    }

    @Builder
    public record PostSummaryDTO(
            Long postId,
            String title,
            Category category,
            Long views,
            Long writerId,
            String writerNickname,
            LocalDateTime createdAt
    ) {
    }

    @Builder
    public record PostPageDTO(
            List<PostSummaryDTO> posts,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean hasNext
    ) {
    }

    // writerId는 프론트에서 로그인 응답의 userId와 비교해 수정/삭제 버튼 노출 여부를 판단하는 용도
    @Builder
    public record PostDetailDTO(
            Long postId,
            String title,
            String contents,
            Category category,
            Long views,
            Long writerId,
            String writerNickname,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<PostImageDTO> images
    ) {
    }

    @Builder
    public record PostImageDTO(
            Long imageId,
            String imageUrl
    ) {
    }
}
