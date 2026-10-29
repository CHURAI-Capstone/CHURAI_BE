package com.site.churaibe.domain.reaction.repository;

import com.site.churaibe.domain.reaction.entity.Reaction;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    boolean existsByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type);

    @Query("select r.type, count(r) from Reaction r where r.post.id = :postId group by r.type")
    List<Object[]> countByType(@Param("postId") Long postId);

    @Query("select r.type from Reaction r where r.post.id = :postId and r.user.id = :userId")
    List<ReactionType> findTypesByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Modifying(flushAutomatically = true)
    @Query("delete from Reaction r where r.post.id = :postId and r.user.id = :userId and r.type in :types")
    void deleteByPostIdAndUserIdAndTypeIn(@Param("postId") Long postId, @Param("userId") Long userId,
                                          @Param("types") Collection<ReactionType> types);

    @Modifying(flushAutomatically = true)
    @Query("delete from Reaction r where r.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);
}
