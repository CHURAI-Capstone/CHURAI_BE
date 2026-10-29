package com.site.churaibe.domain.post.repository;

import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.Spectrum;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

// 값이 없는 조건은 무시하는 검색 조건 모음 (AND로 결합)
public class PostSpecs {
    private static final char ESCAPE = '\\';
    // 조건이 없을 때 쓰는 항상 참인 조건 (Specification.allOf는 null을 받지 않음)
    private static final Specification<Post> ALL = (root, query, cb) -> cb.conjunction();

    public static Specification<Post> search(String keyword, Category category, String tag,
                                             Spectrum spectrum, String coreIngredient) {
        return Specification.allOf(
                keywordLike(keyword),
                categoryEq(category),
                hasTag(tag),
                spectrumEq(spectrum),
                coreIngredientEq(coreIngredient)
        );
    }

    // 음식명(제목), 재료, 태그 중 하나라도 포함하면 검색됨
    private static Specification<Post> keywordLike(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return ALL;
        }
        String pattern = "%" + escapeLike(keyword.trim().toLowerCase()) + "%";

        return (root, query, cb) -> {
            Subquery<Long> ingredientQuery = query.subquery(Long.class);
            Join<Post, String> ingredient = ingredientQuery.correlate(root).join("ingredients");
            ingredientQuery.select(cb.literal(1L)).where(cb.like(cb.lower(ingredient), pattern, ESCAPE));

            Subquery<Long> tagQuery = query.subquery(Long.class);
            Join<Post, String> tag = tagQuery.correlate(root).join("tags");
            tagQuery.select(cb.literal(1L)).where(cb.like(cb.lower(tag), pattern, ESCAPE));

            Predicate titleMatches = cb.like(cb.lower(root.get("title")), pattern, ESCAPE);
            return cb.or(titleMatches, cb.exists(ingredientQuery), cb.exists(tagQuery));
        };
    }

    private static Specification<Post> categoryEq(Category category) {
        return category == null ? ALL : (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    private static Specification<Post> hasTag(String tag) {
        if (!StringUtils.hasText(tag)) {
            return ALL;
        }
        // 저장된 태그와 같은 규칙(# 제거, 소문자)으로 맞춤
        String normalized = tag.trim().replaceFirst("^#+", "").toLowerCase();
        return (root, query, cb) -> cb.isMember(normalized, root.<java.util.Collection<String>>get("tags"));
    }

    private static Specification<Post> spectrumEq(Spectrum spectrum) {
        return spectrum == null ? ALL : (root, query, cb) -> cb.equal(root.get("spectrum"), spectrum);
    }

    private static Specification<Post> coreIngredientEq(String coreIngredient) {
        if (!StringUtils.hasText(coreIngredient)) {
            return ALL;
        }
        return (root, query, cb) -> cb.equal(root.get("coreIngredient"), coreIngredient.trim());
    }

    private static String escapeLike(String value) {
        return value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
