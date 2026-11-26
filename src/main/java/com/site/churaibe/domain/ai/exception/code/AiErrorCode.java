package com.site.churaibe.domain.ai.exception.code;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AiErrorCode implements BaseErrorCode {

    NOT_CONFIGURED(
            HttpStatus.SERVICE_UNAVAILABLE,
            "AI503_1",
            "AI 추천 기능의 서버 설정이 완료되지 않았습니다."
    ),

    PROVIDER_UNAVAILABLE(
            HttpStatus.BAD_GATEWAY,
            "AI502_1",
            "AI 추천 서비스와 통신하지 못했습니다. 잠시 후 다시 시도해주세요."
    ),

    INVALID_RESPONSE(
            HttpStatus.BAD_GATEWAY,
            "AI502_2",
            "AI 추천 결과를 처리하지 못했습니다. 잠시 후 다시 시도해주세요."
    );

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}