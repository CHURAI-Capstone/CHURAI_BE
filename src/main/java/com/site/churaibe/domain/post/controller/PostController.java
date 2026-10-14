package com.site.churaibe.domain.post.controller;

import com.site.churaibe.domain.post.controller.docs.PostControllerDocs;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.exception.code.success.PostSuccessCode;
import com.site.churaibe.domain.post.service.command.PostCommandService;
import com.site.churaibe.domain.post.service.query.PostQueryService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController implements PostControllerDocs {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostResDTO.PostSaveResDTO> createPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @ModelAttribute @Valid PostReqDTO.PostSaveDTO dto) {
        return ApiResponse.onSuccess(PostSuccessCode.POST_CREATED,
                postCommandService.createPost(userDetails.getUser().getId(), dto));
    }

    @Override
    @GetMapping
    public ApiResponse<PostResDTO.PostPageDTO> getPosts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(PostSuccessCode.POST_LIST_SUCCESS, postQueryService.getPosts(page, size));
    }

    @Override
    @GetMapping("/{postId}")
    public ApiResponse<PostResDTO.PostDetailDTO> getPost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(PostSuccessCode.POST_DETAIL_SUCCESS, postQueryService.getPost(postId));
    }

    @Override
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResDTO.PostDetailDTO> updatePost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long postId,
                                                            @ModelAttribute @Valid PostReqDTO.PostUpdateDTO dto) {
        return ApiResponse.onSuccess(PostSuccessCode.POST_UPDATED,
                postCommandService.updatePost(userDetails.getUser().getId(), postId, dto));
    }

    @Override
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable Long postId) {
        postCommandService.deletePost(userDetails.getUser().getId(), postId);
        return ApiResponse.onSuccess(PostSuccessCode.POST_DELETED, null);
    }
}
