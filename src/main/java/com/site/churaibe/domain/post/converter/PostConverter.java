package com.site.churaibe.domain.post.converter;

import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.entity.PostImage;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.domain.scrap.dto.response.ScrapResDTO;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.global.s3.S3Service;
import java.util.List;
import org.springframework.data.domain.Page;

public class PostConverter {

    public static Post toPost(PostReqDTO.PostSaveDTO dto, User writer) {
        return Post.builder()
                .title(dto.title())
                .contents(dto.contents())
                .category(dto.category())
                .summary(dto.summary().trim())
                .spectrum(dto.spectrum())
                .coreIngredient(resolveCoreIngredient(dto.coreIngredient(), normalizeIngredients(dto.ingredients())))
                .ingredients(normalizeIngredients(dto.ingredients()))
                .tags(normalizeTags(dto.tags()))
                .writer(writer)
                .build();
    }

    // 앞뒤 공백을 지우고 중복을 제거 (입력 순서 유지)
    public static List<String> normalizeIngredients(List<String> ingredients) {
        return ingredients.stream().map(String::trim).filter(name -> !name.isEmpty()).distinct().toList();
    }

    // '#' 접두사를 떼고 소문자로 통일, 중복 제거
    public static List<String> normalizeTags(List<String> tags) {
        if (tags == null) {
            return List.of();
        }
        return tags.stream()
                .map(tag -> tag.trim().replaceFirst("^#+", "").trim().toLowerCase())
                .filter(tag -> !tag.isEmpty())
                .distinct()
                .toList();
    }

    public static String resolveCoreIngredient(String coreIngredient, List<String> ingredients) {
        return coreIngredient == null || coreIngredient.isBlank() ? ingredients.get(0) : coreIngredient.trim();
    }

    public static PostResDTO.PostSaveResDTO toPostSaveResDTO(Post post) {
        return PostResDTO.PostSaveResDTO.builder()
                .postId(post.getId())
                .build();
    }

    public static PostResDTO.PostSummaryDTO toPostSummaryDTO(Post post) {
        return PostResDTO.PostSummaryDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .summary(post.getSummary())
                .category(post.getCategory())
                .spectrum(post.getSpectrum())
                .coreIngredient(post.getCoreIngredient())
                .tags(List.copyOf(post.getTags()))
                .views(post.getViews())
                .writerId(post.getWriter().getId())
                .writerNickname(post.getWriter().getNickname())
                .thumbnailUrl(post.getPostImages().stream().findFirst().map(PostImage::getImageUrl).orElse(null))
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static List<PostResDTO.PostSummaryDTO> toPostSummaryDTOs(List<Post> posts) {
        return posts.stream().map(PostConverter::toPostSummaryDTO).toList();
    }

    public static PostResDTO.PostPageDTO toPostPageDTO(Page<Post> posts) {
        return PostResDTO.PostPageDTO.builder()
                .posts(toPostSummaryDTOs(posts.getContent()))
                .page(posts.getNumber())
                .size(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .hasNext(posts.hasNext())
                .build();
    }

    public static PostResDTO.PostDetailDTO toPostDetailDTO(Post post, List<CommentResDTO.CommentDTO> comments,
                                                           ReactionResDTO.ReactionSummaryDTO reactions,
                                                           ScrapResDTO.ScrapDTO scrap) {
        return PostResDTO.PostDetailDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .summary(post.getSummary())
                .contents(post.getContents())
                .category(post.getCategory())
                .spectrum(post.getSpectrum())
                .coreIngredient(post.getCoreIngredient())
                .ingredients(List.copyOf(post.getIngredients()))
                .tags(List.copyOf(post.getTags()))
                .views(post.getViews())
                .writerId(post.getWriter().getId())
                .writerNickname(post.getWriter().getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .images(post.getPostImages().stream().map(PostConverter::toPostImageDTO).toList())
                .reactions(reactions)
                .scrap(scrap)
                .comments(comments)
                .build();
    }

    public static PostResDTO.PostImageDTO toPostImageDTO(PostImage image) {
        return PostResDTO.PostImageDTO.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }

    public static PostImage toPostImage(S3Service.UploadedFile file) {
        return PostImage.builder()
                .imageUrl(file.url())
                .s3Key(file.key())
                .build();
    }
}
