package com.site.churaibe.domain.post.exception.code.success;

import com.site.churaibe.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostSuccessCode implements BaseSuccessCode {
    POST_CREATED(HttpStatus.CREATED,
            "POST201_1",
            "게시글을 작성했습니다."),
    POST_LIST_SUCCESS(HttpStatus.OK,
            "POST200_1",
            "게시글 목록 조회에 성공했습니다."),
    POST_DETAIL_SUCCESS(HttpStatus.OK,
            "POST200_2",
            "게시글 조회에 성공했습니다."),
    POST_UPDATED(HttpStatus.OK,
            "POST200_3",
            "게시글을 수정했습니다."),
    POST_DELETED(HttpStatus.OK,
            "POST200_4",
            "게시글을 삭제했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
