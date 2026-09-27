package com.site.churaibe.domain.user.controller;

import com.site.churaibe.domain.user.controller.docs.UserControllerDocs;
import com.site.churaibe.domain.user.converter.UserConverter;
import com.site.churaibe.domain.user.dto.request.UserReqDTO;
import com.site.churaibe.domain.user.dto.response.UserResDTO;
import com.site.churaibe.domain.user.exception.code.success.UserSuccessCode;
import com.site.churaibe.domain.user.service.UserCommandService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController implements UserControllerDocs {
    private final UserCommandService userCommandService;

    @Override
    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResDTO.SignUpResDTO> signUp(@RequestBody @Valid UserReqDTO.SignUpDTO dto) {
        return ApiResponse.onSuccess(UserSuccessCode.SIGNUP_SUCCESS, userCommandService.signUp(dto));
    }

    @Override
    @PostMapping("/auth/login")
    public ApiResponse<UserResDTO.LoginResDTO> login(@RequestBody @Valid UserReqDTO.LoginDTO dto) {
        return ApiResponse.onSuccess(UserSuccessCode.LOGIN_SUCCESS, userCommandService.login(dto));
    }

    @Override
    @GetMapping("/users/me")
    public ApiResponse<UserResDTO.MyInfoResDTO> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(UserSuccessCode.MY_INFO_SUCCESS, UserConverter.toMyInfoResDTO(userDetails.getUser()));
    }
}
