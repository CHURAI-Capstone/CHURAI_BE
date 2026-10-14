package com.site.churaibe.domain.post.exception.code.error;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
    TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST,
            "POST400_1",
            "이미지는 게시글당 최대 3장까지 첨부할 수 있습니다."),
    INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST,
            "POST400_2",
            "JPG, PNG, GIF, WEBP 형식의 이미지만 업로드할 수 있습니다."),
    NOT_POST_WRITER(HttpStatus.FORBIDDEN,
            "POST403_1",
            "게시글 작성자만 수정하거나 삭제할 수 있습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,
            "POST404_1",
            "게시글을 찾을 수 없습니다."),
    POST_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,
            "POST404_2",
            "삭제하려는 이미지가 해당 게시글에 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
