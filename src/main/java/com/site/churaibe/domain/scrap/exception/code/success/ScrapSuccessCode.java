package com.site.churaibe.domain.scrap.exception.code.success;

import com.site.churaibe.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ScrapSuccessCode implements BaseSuccessCode {
    SCRAP_ADDED(HttpStatus.OK,
            "SCRAP200_1",
            "스크랩했습니다."),
    SCRAP_CANCELED(HttpStatus.OK,
            "SCRAP200_2",
            "스크랩을 취소했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
