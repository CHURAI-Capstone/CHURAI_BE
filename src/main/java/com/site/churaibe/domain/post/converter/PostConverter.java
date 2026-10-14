package com.site.churaibe.domain.post.converter;

import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.entity.PostImage;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.global.s3.S3Service;
import org.springframework.data.domain.Page;

public class PostConverter {

    public static Post toPost(PostReqDTO.PostSaveDTO dto, User writer) {
        return Post.builder()
                .title(dto.title())
                .contents(dto.contents())
                .category(dto.category())
                .writer(writer)
                .build();
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
                .category(post.getCategory())
                .views(post.getViews())
                .writerId(post.getWriter().getId())
                .writerNickname(post.getWriter().getNickname())
                .thumbnailUrl(post.getPostImages().stream().findFirst().map(PostImage::getImageUrl).orElse(null))
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResDTO.PostPageDTO toPostPageDTO(Page<Post> posts) {
        return PostResDTO.PostPageDTO.builder()
                .posts(posts.map(PostConverter::toPostSummaryDTO).getContent())
                .page(posts.getNumber())
                .size(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .hasNext(posts.hasNext())
                .build();
    }

    public static PostResDTO.PostDetailDTO toPostDetailDTO(Post post) {
        return PostResDTO.PostDetailDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .category(post.getCategory())
                .views(post.getViews())
                .writerId(post.getWriter().getId())
                .writerNickname(post.getWriter().getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .images(post.getPostImages().stream().map(PostConverter::toPostImageDTO).toList())
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
