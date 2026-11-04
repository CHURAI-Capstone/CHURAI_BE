package com.site.churaibe.domain.user.converter;

import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.reaction.entity.Reaction;
import com.site.churaibe.domain.user.dto.response.MyPageResDTO;
import org.springframework.data.domain.Page;

public class MyPageConverter {

    public static MyPageResDTO.ReactionHistoryDTO toReactionHistoryDTO(Reaction reaction) {
        return MyPageResDTO.ReactionHistoryDTO.builder()
                .reactionType(reaction.getType())
                .reactionDescription(reaction.getType().getDescription())
                .reactedAt(reaction.getCreatedAt())
                .post(PostConverter.toPostSummaryDTO(reaction.getPost()))
                .build();
    }

    public static MyPageResDTO.ReactionHistoryPageDTO toReactionHistoryPageDTO(Page<Reaction> reactions) {
        return MyPageResDTO.ReactionHistoryPageDTO.builder()
                .reactions(reactions.getContent().stream().map(MyPageConverter::toReactionHistoryDTO).toList())
                .page(reactions.getNumber())
                .size(reactions.getSize())
                .totalElements(reactions.getTotalElements())
                .totalPages(reactions.getTotalPages())
                .hasNext(reactions.hasNext())
                .build();
    }

    public static MyPageResDTO.MyCommentDTO toMyCommentDTO(Comment comment) {
        return MyPageResDTO.MyCommentDTO.builder()
                .commentId(comment.getId())
                .type(comment.getTypeOrDefault())
                .contents(comment.getContents())
                .parentId(comment.getParent() == null ? null : comment.getParent().getId())
                .postId(comment.getPost().getId())
                .postTitle(comment.getPost().getTitle())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static MyPageResDTO.MyCommentPageDTO toMyCommentPageDTO(Page<Comment> comments) {
        return MyPageResDTO.MyCommentPageDTO.builder()
                .comments(comments.getContent().stream().map(MyPageConverter::toMyCommentDTO).toList())
                .page(comments.getNumber())
                .size(comments.getSize())
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .hasNext(comments.hasNext())
                .build();
    }
}
