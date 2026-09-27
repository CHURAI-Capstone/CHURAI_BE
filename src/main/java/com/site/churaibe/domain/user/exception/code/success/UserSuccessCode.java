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
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
