package com.site.churaibe.domain.user.controller.docs;

import com.site.churaibe.domain.user.dto.request.UserReqDTO;
import com.site.churaibe.domain.user.dto.response.UserResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "회원가입 / 로그인 / 내 정보 API")
public interface UserControllerDocs {

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임으로 회원가입합니다. 이미 가입된 이메일이면 409를 반환합니다.")
    ApiResponse<UserResDTO.SignUpResDTO> signUp(UserReqDTO.SignUpDTO dto);

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT 액세스 토큰을 발급합니다.")
    ApiResponse<UserResDTO.LoginResDTO> login(UserReqDTO.LoginDTO dto);

    @Operation(summary = "내 정보 조회", description = "Authorization: Bearer {accessToken} 헤더가 필요합니다. 토큰이 없거나 유효하지 않으면 401을 반환합니다.")
    ApiResponse<UserResDTO.MyInfoResDTO> getMyInfo(@Parameter(hidden = true) CustomUserDetails userDetails);
}
