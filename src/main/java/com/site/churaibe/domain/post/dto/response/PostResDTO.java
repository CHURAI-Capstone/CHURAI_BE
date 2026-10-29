package com.site.churaibe.domain.post.dto.response;

import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.Spectrum;
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
            String summary,
            Category category,
            Spectrum spectrum,
            String coreIngredient,
            List<String> tags,
            Long views,
            Long writerId,
            String writerNickname,
            // 첫 번째 이미지, 이미지가 없으면 null
            String thumbnailUrl,
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
            String summary,
            // 조합 방법
            String contents,
            Category category,
            Spectrum spectrum,
            String coreIngredient,
            List<String> ingredients,
            List<String> tags,
            Long views,
            Long writerId,
            String writerNickname,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<PostImageDTO> images,
            // 최상위 댓글 목록, 각 댓글의 replies에 대댓글이 담김
            List<CommentResDTO.CommentDTO> comments
    ) {
    }

    @Builder
    public record PostImageDTO(
            Long imageId,
            String imageUrl
    ) {
    }

    @Builder
    public record CategoryCountDTO(
            Category category,
            String description,
            long postCount
    ) {
    }

    // 홈 화면: 인기 Top3, 최신 꿀조합, 카테고리 영역
    @Builder
    public record HomeDTO(
            List<PostSummaryDTO> popularPosts,
            List<PostSummaryDTO> latestPosts,
            List<CategoryCountDTO> categories
    ) {
    }

    @Builder
    public record CoreIngredientListDTO(
            List<String> coreIngredients
    ) {
    }

    @Builder
    public record SpectrumGroupDTO(
            Spectrum spectrum,
            String description,
            long totalCount,
            // 인기순 상위 일부
            List<PostSummaryDTO> posts
    ) {
    }

    // 익숙한 조합 → 실험적 조합 순서의 탐색 결과
    @Builder
    public record SpectrumExploreDTO(
            String coreIngredient,
            List<SpectrumGroupDTO> groups
    ) {
    }
}
