package com.site.churaibe.domain.comment.service.query;

import com.site.churaibe.domain.comment.converter.CommentConverter;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.comment.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {
    private final CommentRepository commentRepository;

    public List<CommentResDTO.CommentDTO> getCommentTree(Long postId) {
        return CommentConverter.toCommentTree(commentRepository.findByPostIdOrderByIdAsc(postId));
    }
}
