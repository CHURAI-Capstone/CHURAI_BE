package com.site.churaibe.domain.post.controller;

import com.site.churaibe.domain.post.controller.docs.RankingControllerDocs;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.RankingCriteria;
import com.site.churaibe.domain.post.exception.code.success.PostSuccessCode;
import com.site.churaibe.domain.post.service.query.PostRankingService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rankings")
public class RankingController implements RankingControllerDocs {
    private final PostRankingService postRankingService;

    @Override
    @GetMapping
    public ApiResponse<PostResDTO.RankingPageDTO> getRankings(@RequestParam(defaultValue = "VIEWS") RankingCriteria criteria,
                                                              @RequestParam(required = false) Category category,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(PostSuccessCode.RANKING_SUCCESS,
                postRankingService.getRankings(criteria, category, page, size));
    }
}
