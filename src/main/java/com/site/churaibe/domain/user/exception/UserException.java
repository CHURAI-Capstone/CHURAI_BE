package com.site.churaibe.domain.user.exception;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import com.site.churaibe.global.apiPayload.exception.GeneralException;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}
