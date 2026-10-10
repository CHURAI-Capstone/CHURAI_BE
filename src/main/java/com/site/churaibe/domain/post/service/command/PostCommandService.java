package com.site.churaibe.domain.post.service.command;

import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.exception.PostException;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResDTO.PostSaveResDTO createPost(Long userId, PostReqDTO.PostSaveDTO dto) {
        User writer = userRepository.getReferenceById(userId);
        Post post = postRepository.save(PostConverter.toPost(dto, writer));

        return PostConverter.toPostSaveResDTO(post);
    }

    public PostResDTO.PostDetailDTO updatePost(Long userId, Long postId, PostReqDTO.PostUpdateDTO dto) {
        Post post = getPostWrittenBy(userId, postId);
        post.update(dto.title(), dto.contents(), dto.category());

        // 응답의 updatedAt이 수정 시각으로 채워지도록 먼저 반영
        postRepository.flush();
        return PostConverter.toPostDetailDTO(post);
    }

    public void deletePost(Long userId, Long postId) {
        postRepository.delete(getPostWrittenBy(userId, postId));
    }

    private Post getPostWrittenBy(Long userId, Long postId) {
        Post post = postRepository.findWithWriterById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.isWrittenBy(userId)) {
            throw new PostException(PostErrorCode.NOT_POST_WRITER);
        }
        return post;
    }
}
