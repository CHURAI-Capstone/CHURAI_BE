package com.site.churaibe.domain.reaction.controller;

import com.site.churaibe.domain.reaction.controller.docs.ReactionControllerDocs;
import com.site.churaibe.domain.reaction.dto.request.ReactionReqDTO;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.domain.reaction.enums.ReactionType;
import com.site.churaibe.domain.reaction.exception.code.success.ReactionSuccessCode;
import com.site.churaibe.domain.reaction.service.ReactionService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/reactions")
public class ReactionController implements ReactionControllerDocs {
    private final ReactionService reactionService;

    @Override
    @GetMapping
    public ApiResponse<ReactionResDTO.ReactionSummaryDTO> getReactions(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable Long postId) {
        return ApiResponse.onSuccess(ReactionSuccessCode.REACTION_SUMMARY_SUCCESS,
                reactionService.getReactionSummary(postId, userDetails == null ? null : userDetails.getUser().getId()));
    }

    @Override
    @PostMapping
    public ApiResponse<ReactionResDTO.ReactionSummaryDTO> addReaction(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @PathVariable Long postId,
                                                                     @RequestBody @Valid ReactionReqDTO.ReactionDTO dto) {
        return ApiResponse.onSuccess(ReactionSuccessCode.REACTION_ADDED,
                reactionService.addReaction(userDetails.getUser().getId(), postId, dto.type()));
    }

    @Override
    @DeleteMapping
    public ApiResponse<ReactionResDTO.ReactionSummaryDTO> cancelReaction(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable Long postId,
                                                                        @RequestParam ReactionType type) {
        return ApiResponse.onSuccess(ReactionSuccessCode.REACTION_CANCELED,
                reactionService.cancelReaction(userDetails.getUser().getId(), postId, type));
    }
}
