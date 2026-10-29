package com.site.churaibe.domain.comment.exception.code.error;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {
    INVALID_PARENT_COMMENT(HttpStatus.BAD_REQUEST,
            "COMMENT400_1",
            "같은 게시글의 최상위 댓글에만 대댓글을 달 수 있습니다."),
    NOT_COMMENT_WRITER(HttpStatus.FORBIDDEN,
            "COMMENT403_1",
            "댓글 작성자만 수정하거나 삭제할 수 있습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,
            "COMMENT404_1",
            "댓글을 찾을 수 없습니다."),
    PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,
            "COMMENT404_2",
            "대댓글을 달 부모 댓글을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
