package com.site.churaibe.domain.post.exception;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import com.site.churaibe.global.apiPayload.exception.GeneralException;

public class PostException extends GeneralException {
    public PostException(BaseErrorCode code) {
        super(code);
    }
}
