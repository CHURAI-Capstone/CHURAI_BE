package com.site.churaibe.domain.post.entity;

import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.Spectrum;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

    // 조합 방법
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    // 한 줄 소개, 기존 게시글은 null
    @Column(length = 100)
    private String summary;

    // 핵심 재료 기반 탐색에 쓰는 대표 재료
    @Column(length = 50)
    private String coreIngredient;

    // 익숙한 조합 ~ 실험적 조합, 기존 게시글은 null
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Spectrum spectrum;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "post_ingredients", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "name", nullable = false, length = 50)
    private List<String> ingredients = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "name", nullable = false, length = 30)
    private List<String> tags = new ArrayList<>();

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

    public void update(String title, String contents, Category category, String summary, String coreIngredient,
                       Spectrum spectrum, List<String> ingredients, List<String> tags) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.summary = summary;
        this.coreIngredient = coreIngredient;
        this.spectrum = spectrum;
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public boolean isWrittenBy(Long userId) {
        return writer.getId().equals(userId);
    }
}
