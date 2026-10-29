package com.site.churaibe.domain.scrap.service;

import com.site.churaibe.domain.post.exception.PostException;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.scrap.dto.response.ScrapResDTO;
import com.site.churaibe.domain.scrap.entity.Scrap;
import com.site.churaibe.domain.scrap.repository.ScrapRepository;
import com.site.churaibe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 이미 스크랩한 게시글을 다시 스크랩해도 에러 없이 현재 상태를 돌려줌
    public ScrapResDTO.ScrapDTO addScrap(Long userId, Long postId) {
        validatePostExists(postId);

        if (!scrapRepository.existsByPostIdAndUserId(postId, userId)) {
            scrapRepository.save(Scrap.builder()
                    .post(postRepository.getReferenceById(postId))
                    .user(userRepository.getReferenceById(userId))
                    .build());
        }
        return getScrap(postId, userId);
    }

    // 스크랩하지 않은 게시글을 취소해도 에러 없이 현재 상태를 돌려줌
    public ScrapResDTO.ScrapDTO cancelScrap(Long userId, Long postId) {
        validatePostExists(postId);
        scrapRepository.deleteByPostIdAndUserId(postId, userId);
        return getScrap(postId, userId);
    }

    // 게시글 상세 조회처럼 게시글 존재를 이미 확인한 곳에서 쓰는 조회, userId는 비로그인이면 null
    @Transactional(readOnly = true)
    public ScrapResDTO.ScrapDTO getScrap(Long postId, Long userId) {
        return ScrapResDTO.ScrapDTO.builder()
                .scrapped(userId != null && scrapRepository.existsByPostIdAndUserId(postId, userId))
                .scrapCount(scrapRepository.countByPostId(postId))
                .build();
    }

    private void validatePostExists(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostException(PostErrorCode.POST_NOT_FOUND);
        }
    }
}
