package com.site.churaibe.domain.comment.entity;

import com.site.churaibe.domain.comment.enums.CommentType;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contents;

    // 일반 댓글 / 후기 / 변형 조합, 이 필드가 생기기 전의 기존 댓글은 null (일반 댓글로 취급)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CommentType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 대댓글이면 부모 댓글, 최상위 댓글이면 null (1단계까지만 허용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public CommentType getTypeOrDefault() {
        return type == null ? CommentType.GENERAL : type;
    }

    public boolean isReply() {
        return parent != null;
    }

    public boolean isWrittenBy(Long userId) {
        return writer.getId().equals(userId);
    }
}
