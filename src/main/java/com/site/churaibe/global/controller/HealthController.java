package com.site.churaibe.global.controller;

import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.apiPayload.code.GeneralSuccessCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ApiResponse<String> health() {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, "OK");
    }
}