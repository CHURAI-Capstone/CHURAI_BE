package com.site.churaibe.domain.scrap.controller;

import com.site.churaibe.domain.scrap.controller.docs.ScrapControllerDocs;
import com.site.churaibe.domain.scrap.dto.response.ScrapResDTO;
import com.site.churaibe.domain.scrap.exception.code.success.ScrapSuccessCode;
import com.site.churaibe.domain.scrap.service.ScrapService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/scrap")
public class ScrapController implements ScrapControllerDocs {
    private final ScrapService scrapService;

    @Override
    @PostMapping
    public ApiResponse<ScrapResDTO.ScrapDTO> addScrap(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable Long postId) {
        return ApiResponse.onSuccess(ScrapSuccessCode.SCRAP_ADDED,
                scrapService.addScrap(userDetails.getUser().getId(), postId));
    }

    @Override
    @DeleteMapping
    public ApiResponse<ScrapResDTO.ScrapDTO> cancelScrap(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @PathVariable Long postId) {
        return ApiResponse.onSuccess(ScrapSuccessCode.SCRAP_CANCELED,
                scrapService.cancelScrap(userDetails.getUser().getId(), postId));
    }
}
