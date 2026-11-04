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

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임으로 회원가입합니다. 이미 가입된 이메일이거나 이미 사용 중인 닉네임이면 409를 반환합니다.")
    ApiResponse<UserResDTO.SignUpResDTO> signUp(UserReqDTO.SignUpDTO dto);

    @Operation(summary = "이메일 중복 확인", description = "available이 false이면 이미 가입된 이메일입니다.")
    ApiResponse<UserResDTO.AvailabilityResDTO> checkEmail(@Parameter(description = "확인할 이메일") String email);

    @Operation(summary = "닉네임 중복 확인", description = "available이 false이면 이미 사용 중인 닉네임입니다.")
    ApiResponse<UserResDTO.AvailabilityResDTO> checkNickname(@Parameter(description = "확인할 닉네임") String nickname);

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT 액세스 토큰을 발급합니다.")
    ApiResponse<UserResDTO.LoginResDTO> login(UserReqDTO.LoginDTO dto);

    @Operation(summary = "내 정보 조회", description = "Authorization: Bearer {accessToken} 헤더가 필요합니다. 토큰이 없거나 유효하지 않으면 401을 반환합니다. activity에 마이페이지 상단용 활동 요약(작성한 꿀조합·스크랩·먹어본 꿀조합·맛 평가·댓글 수)이 함께 담깁니다.")
    ApiResponse<UserResDTO.MyInfoResDTO> getMyInfo(@Parameter(hidden = true) CustomUserDetails userDetails);

    @Operation(summary = "프로필 수정", description = "로그인이 필요합니다. 닉네임을 변경하며(앞뒤 공백 제거, 최대 20자), 다른 사용자가 이미 쓰는 닉네임이면 409를 반환합니다. 현재 닉네임과 같은 값을 보내면 변경 없이 성공합니다.")
    ApiResponse<UserResDTO.ProfileUpdateResDTO> updateProfile(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                              UserReqDTO.ProfileUpdateDTO dto);
}
