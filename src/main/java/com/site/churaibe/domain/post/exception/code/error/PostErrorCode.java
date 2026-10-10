package com.site.churaibe.domain.post.exception.code.error;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
    NOT_POST_WRITER(HttpStatus.FORBIDDEN,
            "POST403_1",
            "게시글 작성자만 수정하거나 삭제할 수 있습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,
            "POST404_1",
            "게시글을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
