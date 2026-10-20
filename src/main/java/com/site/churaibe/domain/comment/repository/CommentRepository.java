package com.site.churaibe.domain.comment.repository;

import com.site.churaibe.domain.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 트리를 만들 때 작성자 닉네임을 쓰므로 writer를 함께 조회 (N+1 방지)
    @EntityGraph(attributePaths = "writer")
    List<Comment> findByPostIdOrderByIdAsc(Long postId);

    @EntityGraph(attributePaths = "writer")
    Optional<Comment> findWithWriterByIdAndPostId(Long id, Long postId);

    // 자기참조 FK 때문에 대댓글을 먼저 지운 뒤 나머지를 지워야 함
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.post.id = :postId and c.parent is not null")
    void deleteRepliesByPostId(@Param("postId") Long postId);

    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);

    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.parent.id = :parentId")
    void deleteRepliesByParentId(@Param("parentId") Long parentId);
}
