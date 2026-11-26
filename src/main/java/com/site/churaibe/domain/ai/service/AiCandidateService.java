package com.site.churaibe.domain.ai.service;

import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.PostSort;
import com.site.churaibe.domain.post.repository.PostRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiCandidateService {

    private static final int MAX_CANDIDATES = 40;

    private final PostRepository postRepository;

    /**
     * Gemini에 전달할 실제 게시글 후보를 가져온다.
     *
     * 기존 PostQueryService.getPost()는 조회수를 증가시키므로
     * AI 추천 후보 수집에는 사용하지 않는다.
     *
     * 외부 Gemini API 호출 전에 필요한 데이터만 복사해
     * DB 트랜잭션을 짧게 유지한다.
     */
    @Transactional(readOnly = true)
    public List<Candidate> loadCandidates() {

        return postRepository.findAll(
                        PageRequest.of(
                                0,
                                MAX_CANDIDATES,
                                PostSort.LATEST.getSort()
                        )
                )
                .getContent()
                .stream()
                .map(this::toCandidate)
                .toList();
    }

    private Candidate toCandidate(Post post) {

        return new Candidate(
                PostConverter.toPostSummaryDTO(post),
                List.copyOf(post.getIngredients())
        );
    }

    public record Candidate(
            PostResDTO.PostSummaryDTO post,
            List<String> ingredients
    ) {
    }
}