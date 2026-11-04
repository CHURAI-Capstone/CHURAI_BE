package com.site.churaibe.domain.user.exception.code.success;

import com.site.churaibe.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserSuccessCode implements BaseSuccessCode {
    SIGNUP_SUCCESS(HttpStatus.CREATED,
            "USER201_1",
            "회원가입에 성공했습니다."),
    LOGIN_SUCCESS(HttpStatus.OK,
            "USER200_1",
            "로그인에 성공했습니다."),
    MY_INFO_SUCCESS(HttpStatus.OK,
            "USER200_2",
            "내 정보 조회에 성공했습니다."),
    AVAILABILITY_CHECK_SUCCESS(HttpStatus.OK,
            "USER200_3",
            "중복 확인에 성공했습니다."),
    PROFILE_UPDATE_SUCCESS(HttpStatus.OK,
            "USER200_4",
            "프로필 수정에 성공했습니다."),
    MY_POSTS_SUCCESS(HttpStatus.OK,
            "USER200_5",
            "내가 작성한 꿀조합 조회에 성공했습니다."),
    MY_SCRAPS_SUCCESS(HttpStatus.OK,
            "USER200_6",
            "스크랩 목록 조회에 성공했습니다."),
    MY_TRIED_SUCCESS(HttpStatus.OK,
            "USER200_7",
            "먹어본 꿀조합 조회에 성공했습니다."),
    MY_REACTIONS_SUCCESS(HttpStatus.OK,
            "USER200_8",
            "맛 평가 내역 조회에 성공했습니다."),
    MY_COMMENTS_SUCCESS(HttpStatus.OK,
            "USER200_9",
            "댓글·후기 내역 조회에 성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
