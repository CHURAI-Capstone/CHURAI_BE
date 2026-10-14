package com.site.churaibe.domain.post.entity;

import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "posts")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    // 증가는 PostRepository.increaseViews(벌크 update)로만 처리
    @Builder.Default
    @Column(nullable = false)
    private Long views = 0L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    // 목록에서 썸네일(첫 이미지)을 읽을 때 게시글마다 쿼리가 나가지 않도록 묶어서 조회
    @Builder.Default
    @BatchSize(size = 50)
    @OrderBy("id ASC")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    public void addImage(PostImage image) {
        image.assignPost(this);
        postImages.add(image);
    }

    // 지정한 이미지를 게시글에서 빼고, S3에서 지울 키를 돌려줌
    public List<String> removeImages(Collection<Long> imageIds) {
        List<PostImage> removed = postImages.stream()
                .filter(image -> imageIds.contains(image.getId()))
                .toList();
        postImages.removeAll(removed);
        return removed.stream().map(PostImage::getS3Key).toList();
    }

    public void update(String title, String contents, Category category) {
        this.title = title;
        this.contents = contents;
        this.category = category;
    }

    public boolean isWrittenBy(Long userId) {
        return writer.getId().equals(userId);
    }
}
