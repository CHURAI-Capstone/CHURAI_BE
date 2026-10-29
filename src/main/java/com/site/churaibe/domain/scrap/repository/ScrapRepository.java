package com.site.churaibe.domain.scrap.repository;

import com.site.churaibe.domain.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    long countByPostId(Long postId);

    @Modifying(flushAutomatically = true)
    @Query("delete from Scrap s where s.post.id = :postId and s.user.id = :userId")
    void deleteByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Modifying(flushAutomatically = true)
    @Query("delete from Scrap s where s.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);
}
