package com.site.churaibe.domain.user.controller;

import com.site.churaibe.domain.comment.enums.CommentType;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import com.site.churaibe.domain.user.controller.docs.MyPageControllerDocs;
import com.site.churaibe.domain.user.dto.response.MyPageResDTO;
import com.site.churaibe.domain.user.exception.code.success.UserSuccessCode;
import com.site.churaibe.domain.user.service.UserQueryService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/me")
public class MyPageController implements MyPageControllerDocs {
    private final UserQueryService userQueryService;

    @Override
    @GetMapping("/posts")
    public ApiResponse<PostResDTO.PostPageDTO> getMyPosts(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(UserSuccessCode.MY_POSTS_SUCCESS,
                userQueryService.getMyPosts(userDetails.getUser().getId(), page, size));
    }

    @Override
    @GetMapping("/scraps")
    public ApiResponse<PostResDTO.PostPageDTO> getMyScraps(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(UserSuccessCode.MY_SCRAPS_SUCCESS,
                userQueryService.getMyScraps(userDetails.getUser().getId(), page, size));
    }

    @Override
    @GetMapping("/tried")
    public ApiResponse<PostResDTO.PostPageDTO> getMyTriedPosts(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(UserSuccessCode.MY_TRIED_SUCCESS,
                userQueryService.getMyTriedPosts(userDetails.getUser().getId(), page, size));
    }

    @Override
    @GetMapping("/reactions")
    public ApiResponse<MyPageResDTO.ReactionHistoryPageDTO> getMyReactions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) ReactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(UserSuccessCode.MY_REACTIONS_SUCCESS,
                userQueryService.getMyReactions(userDetails.getUser().getId(), type, page, size));
    }

    @Override
    @GetMapping("/comments")
    public ApiResponse<MyPageResDTO.MyCommentPageDTO> getMyComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) CommentType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(UserSuccessCode.MY_COMMENTS_SUCCESS,
                userQueryService.getMyComments(userDetails.getUser().getId(), type, page, size));
    }
}
