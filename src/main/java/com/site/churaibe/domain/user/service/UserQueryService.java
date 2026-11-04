package com.site.churaibe.domain.user.service;

import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.comment.enums.CommentType;
import com.site.churaibe.domain.comment.repository.CommentRepository;
import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.reaction.entity.Reaction;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import com.site.churaibe.domain.reaction.repository.ReactionRepository;
import com.site.churaibe.domain.scrap.entity.Scrap;
import com.site.churaibe.domain.scrap.repository.ScrapRepository;
import com.site.churaibe.domain.user.converter.MyPageConverter;
import com.site.churaibe.domain.user.converter.UserConverter;
import com.site.churaibe.domain.user.dto.response.MyPageResDTO;
import com.site.churaibe.domain.user.dto.response.UserResDTO;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.domain.user.exception.UserException;
import com.site.churaibe.domain.user.exception.code.error.UserErrorCode;
import com.site.churaibe.domain.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 마이페이지: 프로필과 개인 활동 내역 조회. 모든 목록은 최근 활동 순이고 page는 0부터 시작
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
    private static final int MAX_PAGE_SIZE = 50;
    private static final List<ReactionType> TRIED_TYPES = List.of(ReactionType.TRIED);
    private static final List<ReactionType> TASTE_TYPES =
            Arrays.stream(ReactionType.values()).filter(ReactionType::isTaste).toList();

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ScrapRepository scrapRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;

    public UserResDTO.MyInfoResDTO getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return UserConverter.toMyInfoResDTO(user, UserResDTO.ActivityCountDTO.builder()
                .postCount(postRepository.countByWriterId(userId))
                .scrapCount(scrapRepository.countByUserId(userId))
                .triedCount(reactionRepository.countByUserIdAndTypeIn(userId, TRIED_TYPES))
                .tasteReactionCount(reactionRepository.countByUserIdAndTypeIn(userId, TASTE_TYPES))
                .commentCount(commentRepository.countByWriterId(userId))
                .build());
    }

    public PostResDTO.PostPageDTO getMyPosts(Long userId, int page, int size) {
        return PostConverter.toPostPageDTO(postRepository.findByWriterId(userId, pageRequest(page, size)));
    }

    public PostResDTO.PostPageDTO getMyScraps(Long userId, int page, int size) {
        return PostConverter.toPostPageDTO(scrapRepository.findByUserId(userId, pageRequest(page, size))
                .map(Scrap::getPost));
    }

    // 먹어봤어요를 누른 꿀조합
    public PostResDTO.PostPageDTO getMyTriedPosts(Long userId, int page, int size) {
        return PostConverter.toPostPageDTO(
                reactionRepository.findByUserIdAndTypeIn(userId, TRIED_TYPES, pageRequest(page, size))
                        .map(Reaction::getPost));
    }

    // type을 비우면 맛 평가 3종(맛있었어요/맛이 특이해요/내 취향은 아니에요) 전체
    public MyPageResDTO.ReactionHistoryPageDTO getMyReactions(Long userId, ReactionType type, int page, int size) {
        List<ReactionType> types = type == null ? TASTE_TYPES : List.of(type);
        return MyPageConverter.toReactionHistoryPageDTO(
                reactionRepository.findByUserIdAndTypeIn(userId, types, pageRequest(page, size)));
    }

    // type을 비우면 일반 댓글·후기·변형 조합 전체
    public MyPageResDTO.MyCommentPageDTO getMyComments(Long userId, CommentType type, int page, int size) {
        Pageable pageable = pageRequest(page, size);
        Page<Comment> comments;
        if (type == null) {
            comments = commentRepository.findByWriterId(userId, pageable);
        } else if (type == CommentType.GENERAL) {
            comments = commentRepository.findGeneralByWriterId(userId, pageable);
        } else {
            comments = commentRepository.findByWriterIdAndType(userId, type, pageable);
        }
        return MyPageConverter.toMyCommentPageDTO(comments);
    }

    private Pageable pageRequest(int page, int size) {
        return PageRequest.of(Math.max(page, 0), Math.clamp(size, 1, MAX_PAGE_SIZE), Sort.by(Sort.Direction.DESC, "id"));
    }
}
