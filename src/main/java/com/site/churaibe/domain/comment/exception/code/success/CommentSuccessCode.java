package com.site.churaibe.domain.comment.exception.code.success;

import com.site.churaibe.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentSuccessCode implements BaseSuccessCode {
    COMMENT_CREATED(HttpStatus.CREATED,
            "COMMENT201_1",
            "댓글을 작성했습니다."),
    COMMENT_DELETED(HttpStatus.OK,
            "COMMENT200_1",
            "댓글을 삭제했습니다."),
    COMMENT_UPDATED(HttpStatus.OK,
            "COMMENT200_2",
            "댓글을 수정했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
