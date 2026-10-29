package com.site.churaibe.domain.post.repository;

import com.site.churaibe.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    // 목록에서 작성자 닉네임을 쓰므로 writer를 함께 조회 (N+1 방지)
    @Override
    @EntityGraph(attributePaths = "writer")
    Page<Post> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "writer")
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"writer", "postImages"})
    Optional<Post> findWithWriterById(Long id);

    // 동시 조회 시 증가분이 유실되지 않도록 DB에서 바로 +1 (updated_at은 바뀌지 않음)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.views = p.views + 1 where p.id = :postId")
    int increaseViews(@Param("postId") Long postId);

    @Query("select p.category, count(p) from Post p group by p.category")
    List<Object[]> countByCategory();

    // 핵심 재료별 꿀조합 수, 많은 순
    @Query("select p.coreIngredient from Post p where p.coreIngredient is not null group by p.coreIngredient order by count(p) desc, p.coreIngredient")
    List<String> findCoreIngredients(Pageable pageable);
}
