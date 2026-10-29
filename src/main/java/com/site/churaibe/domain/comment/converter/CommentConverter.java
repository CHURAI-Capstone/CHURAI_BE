package com.site.churaibe.domain.comment.converter;

import com.site.churaibe.domain.comment.dto.request.CommentReqDTO;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.comment.enums.CommentType;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.user.entity.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentConverter {

    public static Comment toComment(CommentReqDTO.CommentSaveDTO dto, Post post, Comment parent, User writer) {
        return Comment.builder()
                .contents(dto.contents())
                .type(parent != null || dto.type() == null ? CommentType.GENERAL : dto.type())
                .post(post)
                .parent(parent)
                .writer(writer)
                .build();
    }

    public static CommentResDTO.CommentSaveResDTO toCommentSaveResDTO(Comment comment) {
        return CommentResDTO.CommentSaveResDTO.builder()
                .commentId(comment.getId())
                .build();
    }

    // 오래된 순으로 정렬된 게시글의 전체 댓글을 최상위 댓글 + replies 구조로 묶음
    public static List<CommentResDTO.CommentDTO> toCommentTree(List<Comment> comments) {
        Map<Long, List<Comment>> repliesByParentId = comments.stream()
                .filter(Comment::isReply)
                .collect(Collectors.groupingBy(comment -> comment.getParent().getId()));

        return comments.stream()
                .filter(comment -> !comment.isReply())
                .map(comment -> toCommentDTO(comment, repliesByParentId.getOrDefault(comment.getId(), List.of())
                        .stream()
                        .map(reply -> toCommentDTO(reply, List.of()))
                        .toList()))
                .toList();
    }

    private static CommentResDTO.CommentDTO toCommentDTO(Comment comment, List<CommentResDTO.CommentDTO> replies) {
        return CommentResDTO.CommentDTO.builder()
                .commentId(comment.getId())
                .type(comment.getTypeOrDefault())
                .contents(comment.getContents())
                .writerId(comment.getWriter().getId())
                .writerNickname(comment.getWriter().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .replies(replies)
                .build();
    }
}
