package com.site.churaibe.domain.post.controller;

import com.site.churaibe.domain.post.controller.docs.DiscoveryControllerDocs;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.exception.code.success.PostSuccessCode;
import com.site.churaibe.domain.post.service.query.PostDiscoveryService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DiscoveryController implements DiscoveryControllerDocs {
    private final PostDiscoveryService postDiscoveryService;

    @Override
    @GetMapping("/home")
    public ApiResponse<PostResDTO.HomeDTO> getHome() {
        return ApiResponse.onSuccess(PostSuccessCode.HOME_SUCCESS, postDiscoveryService.getHome());
    }

    @Override
    @GetMapping("/explore/core-ingredients")
    public ApiResponse<PostResDTO.CoreIngredientListDTO> getCoreIngredients() {
        return ApiResponse.onSuccess(PostSuccessCode.CORE_INGREDIENTS_SUCCESS, postDiscoveryService.getCoreIngredients());
    }

    @Override
    @GetMapping("/explore/spectrum")
    public ApiResponse<PostResDTO.SpectrumExploreDTO> exploreSpectrum(
            @RequestParam String coreIngredient,
            @RequestParam(defaultValue = "5") int size) {
        return ApiResponse.onSuccess(PostSuccessCode.SPECTRUM_EXPLORE_SUCCESS,
                postDiscoveryService.exploreSpectrum(coreIngredient, size));
    }
}
