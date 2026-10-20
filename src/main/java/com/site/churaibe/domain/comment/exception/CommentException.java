package com.site.churaibe.domain.comment.exception;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import com.site.churaibe.global.apiPayload.exception.GeneralException;

public class CommentException extends GeneralException {
    public CommentException(BaseErrorCode code) {
        super(code);
    }
}
