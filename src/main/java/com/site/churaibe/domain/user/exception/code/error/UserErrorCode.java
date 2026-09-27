package com.site.churaibe.domain.user.exception.code.error;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED,
            "USER401_1",
            "이메일 또는 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,
            "USER404_1",
            "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT,
            "USER409_1",
            "이미 가입된 이메일입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
