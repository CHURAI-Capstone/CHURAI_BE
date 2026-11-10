package com.site.churaibe.domain.post.service.query;

import com.site.churaibe.domain.comment.repository.CommentRepository;
import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.RankingCriteria;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.post.repository.PostSpecs;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import com.site.churaibe.domain.reaction.repository.ReactionRepository;
import com.site.churaibe.domain.scrap.repository.ScrapRepository;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 인기 랭킹: 기준별 순위와 게시글별 사용자 반응 데이터
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostRankingService {
    private static final int MAX_PAGE_SIZE = 50;

    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    private final ScrapRepository scrapRepository;
    private final CommentRepository commentRepository;

    // category는 선택, page는 0부터 시작
    public PostResDTO.RankingPageDTO getRankings(RankingCriteria criteria, Category category, int page, int size) {
        RankingCriteria rankBy = criteria == null ? RankingCriteria.VIEWS : criteria;
        int pageNumber = Math.max(page, 0);
        int pageSize = Math.clamp(size, 1, MAX_PAGE_SIZE);

        Page<Post> posts = postRepository.findAll(PostSpecs.rankedBy(rankBy, category),
                PageRequest.of(pageNumber, pageSize));
        List<Post> content = posts.getContent();

        List<Long> postIds = content.stream().map(Post::getId).toList();
        Map<Long, Map<ReactionType, Long>> reactionCounts = countReactions(postIds);
        Map<Long, Long> scrapCounts = postIds.isEmpty() ? Map.of() : toCountMap(scrapRepository.countByPostIds(postIds));
        Map<Long, Long> commentCounts = postIds.isEmpty() ? Map.of() : toCountMap(commentRepository.countByPostIds(postIds));

        List<PostResDTO.RankedPostDTO> rankings = IntStream.range(0, content.size())
                .mapToObj(i -> {
                    Post post = content.get(i);
                    return PostResDTO.RankedPostDTO.builder()
                            .rank(pageNumber * pageSize + i + 1)
                            .post(PostConverter.toPostSummaryDTO(post))
                            .reactionCounts(reactionCounts.get(post.getId()))
                            .scrapCount(scrapCounts.getOrDefault(post.getId(), 0L))
                            .commentCount(commentCounts.getOrDefault(post.getId(), 0L))
                            .build();
                })
                .toList();

        return PostResDTO.RankingPageDTO.builder()
                .criteria(rankBy)
                .category(category)
                .rankings(rankings)
                .page(posts.getNumber())
                .size(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .hasNext(posts.hasNext())
                .build();
    }

    // 반응이 없는 게시글/종류도 0건으로 채움
    private Map<Long, Map<ReactionType, Long>> countReactions(Collection<Long> postIds) {
        Map<Long, Map<ReactionType, Long>> result = new HashMap<>();
        for (Long postId : postIds) {
            Map<ReactionType, Long> counts = new EnumMap<>(ReactionType.class);
            for (ReactionType type : ReactionType.values()) {
                counts.put(type, 0L);
            }
            result.put(postId, counts);
        }
        if (!postIds.isEmpty()) {
            for (Object[] row : reactionRepository.countByPostIds(postIds)) {
                result.get((Long) row[0]).put((ReactionType) row[1], (Long) row[2]);
            }
        }
        return result;
    }

    private Map<Long, Long> toCountMap(List<Object[]> rows) {
        Map<Long, Long> result = new HashMap<>();
        for (Object[] row : rows) {
            result.put((Long) row[0], (Long) row[1]);
        }
        return result;
    }
}
