package com.site.churaibe.domain.post.service.command;

import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.entity.PostImage;
import com.site.churaibe.domain.post.exception.PostException;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.domain.user.repository.UserRepository;
import com.site.churaibe.global.s3.S3Service;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandService {
    private static final int MAX_IMAGE_COUNT = 3;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public PostResDTO.PostSaveResDTO createPost(Long userId, PostReqDTO.PostSaveDTO dto) {
        List<MultipartFile> images = validateNewImages(dto.images(), 0);

        User writer = userRepository.getReferenceById(userId);
        Post post = PostConverter.toPost(dto, writer);
        addImages(post, images);
        postRepository.save(post);

        return PostConverter.toPostSaveResDTO(post);
    }

    public PostResDTO.PostDetailDTO updatePost(Long userId, Long postId, PostReqDTO.PostUpdateDTO dto) {
        Post post = getPostWrittenBy(userId, postId);
        List<Long> deleteImageIds = dto.deleteImageIds() == null ? List.of() : dto.deleteImageIds();
        validateDeleteImageIds(post, deleteImageIds);
        List<MultipartFile> newImages = validateNewImages(dto.images(), post.getPostImages().size() - deleteImageIds.size());

        post.update(dto.title(), dto.contents(), dto.category());
        List<String> removedKeys = post.removeImages(deleteImageIds);
        addImages(post, newImages);

        // 응답의 updatedAt이 수정 시각으로 채워지도록 먼저 반영
        postRepository.flush();
        s3Service.deleteAfterCommit(removedKeys);
        return PostConverter.toPostDetailDTO(post);
    }

    public void deletePost(Long userId, Long postId) {
        Post post = getPostWrittenBy(userId, postId);
        List<String> imageKeys = post.getPostImages().stream().map(PostImage::getS3Key).toList();

        postRepository.delete(post);
        s3Service.deleteAfterCommit(imageKeys);
    }

    // 빈 파일(Swagger가 비어 있는 파트를 보내는 경우)은 무시하고, 개수와 형식을 검사
    private List<MultipartFile> validateNewImages(List<MultipartFile> images, int remainingCount) {
        List<MultipartFile> files = images == null ? List.of() : images.stream().filter(file -> !file.isEmpty()).toList();

        if (remainingCount + files.size() > MAX_IMAGE_COUNT) {
            throw new PostException(PostErrorCode.TOO_MANY_IMAGES);
        }
        if (!files.stream().allMatch(S3Service::isSupportedImage)) {
            throw new PostException(PostErrorCode.INVALID_IMAGE_TYPE);
        }
        return files;
    }

    private void validateDeleteImageIds(Post post, List<Long> deleteImageIds) {
        HashSet<Long> ownImageIds = new HashSet<>(post.getPostImages().stream().map(PostImage::getId).toList());
        if (!ownImageIds.containsAll(deleteImageIds)) {
            throw new PostException(PostErrorCode.POST_IMAGE_NOT_FOUND);
        }
    }

    private void addImages(Post post, List<MultipartFile> files) {
        if (files.isEmpty()) {
            return;
        }
        s3Service.upload(files).forEach(uploaded -> post.addImage(PostConverter.toPostImage(uploaded)));
    }

    private Post getPostWrittenBy(Long userId, Long postId) {
        Post post = postRepository.findWithWriterById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.isWrittenBy(userId)) {
            throw new PostException(PostErrorCode.NOT_POST_WRITER);
        }
        return post;
    }
}
