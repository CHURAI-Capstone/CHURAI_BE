package com.site.churaibe.domain.comment.service.command;

import com.site.churaibe.domain.comment.converter.CommentConverter;
import com.site.churaibe.domain.comment.dto.request.CommentReqDTO;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.comment.exception.CommentException;
import com.site.churaibe.domain.comment.exception.code.error.CommentErrorCode;
import com.site.churaibe.domain.comment.repository.CommentRepository;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.exception.PostException;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResDTO.CommentSaveResDTO createComment(Long userId, Long postId, CommentReqDTO.CommentSaveDTO dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Comment parent = dto.parentId() == null ? null : getParent(postId, dto.parentId());

        Comment comment = CommentConverter.toComment(dto, post, parent, userRepository.getReferenceById(userId));
        commentRepository.save(comment);

        return CommentConverter.toCommentSaveResDTO(comment);
    }

    public CommentResDTO.CommentSaveResDTO updateComment(Long userId, Long postId, Long commentId,
                                                         CommentReqDTO.CommentUpdateDTO dto) {
        Comment comment = getCommentWrittenBy(userId, postId, commentId);
        comment.updateContents(dto.contents());
        // 수정 시각이 반영되도록 먼저 쓰기
        commentRepository.flush();

        return CommentConverter.toCommentSaveResDTO(comment);
    }

    // 최상위 댓글을 지우면 달린 대댓글도 함께 지움
    public void deleteComment(Long userId, Long postId, Long commentId) {
        Comment comment = getCommentWrittenBy(userId, postId, commentId);

        if (!comment.isReply()) {
            commentRepository.deleteRepliesByParentId(commentId);
        }
        commentRepository.delete(comment);
    }

    private Comment getCommentWrittenBy(Long userId, Long postId, Long commentId) {
        Comment comment = commentRepository.findWithWriterByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (!comment.isWrittenBy(userId)) {
            throw new CommentException(CommentErrorCode.NOT_COMMENT_WRITER);
        }
        return comment;
    }

    private Comment getParent(Long postId, Long parentId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));

        if (parent.isReply() || !parent.getPost().getId().equals(postId)) {
            throw new CommentException(CommentErrorCode.INVALID_PARENT_COMMENT);
        }
        return parent;
    }
}
