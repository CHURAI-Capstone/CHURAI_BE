package com.site.churaibe.domain.post.service.query;

import com.site.churaibe.domain.comment.service.query.CommentQueryService;
import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.exception.PostException;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {
    private static final int MAX_PAGE_SIZE = 50;

    private final PostRepository postRepository;
    private final CommentQueryService commentQueryService;

    // 최신순, page는 0부터 시작
    public PostResDTO.PostPageDTO getPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                Math.max(page, 0),
                Math.clamp(size, 1, MAX_PAGE_SIZE),
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );

        return PostConverter.toPostPageDTO(postRepository.findAll(pageRequest));
    }

    // 상세 조회 시 조회수 +1 이 함께 일어나므로 쓰기 트랜잭션
    @Transactional
    public PostResDTO.PostDetailDTO getPost(Long postId) {
        if (postRepository.increaseViews(postId) == 0) {
            throw new PostException(PostErrorCode.POST_NOT_FOUND);
        }

        Post post = postRepository.findWithWriterById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        return PostConverter.toPostDetailDTO(post, commentQueryService.getCommentTree(postId));
    }
}
