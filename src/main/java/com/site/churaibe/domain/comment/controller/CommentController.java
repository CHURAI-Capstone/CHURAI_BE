package com.site.churaibe.domain.comment.controller;

import com.site.churaibe.domain.comment.controller.docs.CommentControllerDocs;
import com.site.churaibe.domain.comment.dto.request.CommentReqDTO;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.comment.exception.code.success.CommentSuccessCode;
import com.site.churaibe.domain.comment.service.command.CommentCommandService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController implements CommentControllerDocs {
    private final CommentCommandService commentCommandService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentResDTO.CommentSaveResDTO> createComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable Long postId,
                                                                      @RequestBody @Valid CommentReqDTO.CommentSaveDTO dto) {
        return ApiResponse.onSuccess(CommentSuccessCode.COMMENT_CREATED,
                commentCommandService.createComment(userDetails.getUser().getId(), postId, dto));
    }

    @Override
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Long postId,
                                           @PathVariable Long commentId) {
        commentCommandService.deleteComment(userDetails.getUser().getId(), postId, commentId);
        return ApiResponse.onSuccess(CommentSuccessCode.COMMENT_DELETED, null);
    }
}
