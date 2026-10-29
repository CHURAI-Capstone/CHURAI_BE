package com.site.churaibe.domain.post.service.query;

import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.PostSort;
import com.site.churaibe.domain.post.enums.Spectrum;
import com.site.churaibe.domain.post.exception.PostException;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.post.repository.PostSpecs;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 홈 화면과 핵심 재료 기반 스펙트럼 탐색
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostDiscoveryService {
    private static final int POPULAR_COUNT = 3;
    private static final int LATEST_COUNT = 10;
    private static final int CORE_INGREDIENT_COUNT = 20;
    private static final int MAX_SPECTRUM_POSTS = 20;

    private final PostRepository postRepository;

    public PostResDTO.HomeDTO getHome() {
        return PostResDTO.HomeDTO.builder()
                .popularPosts(PostConverter.toPostSummaryDTOs(
                        postRepository.findAll(PageRequest.of(0, POPULAR_COUNT, PostSort.POPULAR.getSort())).getContent()))
                .latestPosts(PostConverter.toPostSummaryDTOs(
                        postRepository.findAll(PageRequest.of(0, LATEST_COUNT, PostSort.LATEST.getSort())).getContent()))
                .categories(getCategories())
                .build();
    }

    public PostResDTO.CoreIngredientListDTO getCoreIngredients() {
        return PostResDTO.CoreIngredientListDTO.builder()
                .coreIngredients(postRepository.findCoreIngredients(PageRequest.of(0, CORE_INGREDIENT_COUNT)))
                .build();
    }

    // 핵심 재료 하나를 기준으로 스펙트럼 단계별 꿀조합을 인기순으로 묶어서 반환
    public PostResDTO.SpectrumExploreDTO exploreSpectrum(String coreIngredient, int size) {
        if (coreIngredient.isBlank()) {
            throw new PostException(PostErrorCode.CORE_INGREDIENT_REQUIRED);
        }
        PageRequest pageRequest = PageRequest.of(0, Math.clamp(size, 1, MAX_SPECTRUM_POSTS), PostSort.POPULAR.getSort());

        List<PostResDTO.SpectrumGroupDTO> groups = Arrays.stream(Spectrum.values())
                .map(spectrum -> {
                    Page<Post> posts = postRepository.findAll(
                            PostSpecs.search(null, null, null, spectrum, coreIngredient), pageRequest);
                    return PostResDTO.SpectrumGroupDTO.builder()
                            .spectrum(spectrum)
                            .description(spectrum.getDescription())
                            .totalCount(posts.getTotalElements())
                            .posts(PostConverter.toPostSummaryDTOs(posts.getContent()))
                            .build();
                })
                .toList();

        return PostResDTO.SpectrumExploreDTO.builder()
                .coreIngredient(coreIngredient.trim())
                .groups(groups)
                .build();
    }

    // 글이 없는 카테고리도 0건으로 내려 홈 화면에 항상 모든 카테고리가 보이도록 함
    private List<PostResDTO.CategoryCountDTO> getCategories() {
        Map<Category, Long> counts = new EnumMap<>(Category.class);
        for (Object[] row : postRepository.countByCategory()) {
            counts.put((Category) row[0], (Long) row[1]);
        }

        return Arrays.stream(Category.values())
                .map(category -> PostResDTO.CategoryCountDTO.builder()
                        .category(category)
                        .description(category.getDescription())
                        .postCount(counts.getOrDefault(category, 0L))
                        .build())
                .toList();
    }
}
