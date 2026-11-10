package com.site.churaibe.domain.scrap.repository;

import com.site.churaibe.domain.scrap.entity.Scrap;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    long countByPostId(Long postId);

    // 랭킹: 여러 게시글의 스크랩 수를 한 번에 조회 → [postId, count]
    @Query("select s.post.id, count(s) from Scrap s where s.post.id in :postIds group by s.post.id")
    List<Object[]> countByPostIds(@Param("postIds") Collection<Long> postIds);

    // 마이페이지: 목록에서 게시글과 작성자 닉네임을 쓰므로 함께 조회 (N+1 방지)
    @EntityGraph(attributePaths = {"post", "post.writer"})
    Page<Scrap> findByUserId(Long userId, Pageable pageable);

    long countByUserId(Long userId);

    @Modifying(flushAutomatically = true)
    @Query("delete from Scrap s where s.post.id = :postId and s.user.id = :userId")
    void deleteByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Modifying(flushAutomatically = true)
    @Query("delete from Scrap s where s.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);
}
