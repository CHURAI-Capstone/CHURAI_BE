package com.site.churaibe.domain.reaction.exception.code.success;

import com.site.churaibe.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReactionSuccessCode implements BaseSuccessCode {
    REACTION_SUMMARY_SUCCESS(HttpStatus.OK,
            "REACTION200_1",
            "반응 조회에 성공했습니다."),
    REACTION_ADDED(HttpStatus.OK,
            "REACTION200_2",
            "반응을 등록했습니다."),
    REACTION_CANCELED(HttpStatus.OK,
            "REACTION200_3",
            "반응을 취소했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
