package com.site.churaibe.domain.reaction.service;

import com.site.churaibe.domain.post.exception.PostException;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.domain.reaction.entity.Reaction;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import com.site.churaibe.domain.reaction.repository.ReactionRepository;
import com.site.churaibe.domain.user.repository.UserRepository;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 이미 선택한 반응을 다시 보내도 에러 없이 현재 상태를 돌려줌
    // 맛 평가(맛있었어요/맛이 특이해요/내 취향은 아니에요)는 하나만 유지되므로 다른 맛 평가가 있으면 바뀜
    public ReactionResDTO.ReactionSummaryDTO addReaction(Long userId, Long postId, ReactionType type) {
        validatePostExists(postId);

        if (type.isTaste()) {
            reactionRepository.deleteByPostIdAndUserIdAndTypeIn(postId, userId, type.otherTastes());
        }
        if (!reactionRepository.existsByPostIdAndUserIdAndType(postId, userId, type)) {
            reactionRepository.save(Reaction.builder()
                    .post(postRepository.getReferenceById(postId))
                    .user(userRepository.getReferenceById(userId))
                    .type(type)
                    .build());
        }
        return getSummary(postId, userId);
    }

    // 선택하지 않은 반응을 취소해도 에러 없이 현재 상태를 돌려줌
    public ReactionResDTO.ReactionSummaryDTO cancelReaction(Long userId, Long postId, ReactionType type) {
        validatePostExists(postId);
        reactionRepository.deleteByPostIdAndUserIdAndTypeIn(postId, userId, List.of(type));
        return getSummary(postId, userId);
    }

    @Transactional(readOnly = true)
    public ReactionResDTO.ReactionSummaryDTO getReactionSummary(Long postId, Long userId) {
        validatePostExists(postId);
        return getSummary(postId, userId);
    }

    // 게시글 상세 조회처럼 게시글 존재를 이미 확인한 곳에서 쓰는 조회, userId는 비로그인이면 null
    @Transactional(readOnly = true)
    public ReactionResDTO.ReactionSummaryDTO getSummary(Long postId, Long userId) {
        Map<ReactionType, Long> counts = new EnumMap<>(ReactionType.class);
        Arrays.stream(ReactionType.values()).forEach(type -> counts.put(type, 0L));
        for (Object[] row : reactionRepository.countByType(postId)) {
            counts.put((ReactionType) row[0], (Long) row[1]);
        }

        List<ReactionType> myReactions = userId == null
                ? List.of()
                : reactionRepository.findTypesByPostIdAndUserId(postId, userId).stream().sorted().toList();

        return ReactionResDTO.ReactionSummaryDTO.builder()
                .counts(counts)
                .myReactions(myReactions)
                .build();
    }

    private void validatePostExists(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostException(PostErrorCode.POST_NOT_FOUND);
        }
    }
}
