package com.site.churaibe.global.apiPayload.handler;

import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import com.site.churaibe.global.apiPayload.code.GeneralErrorCode;
import com.site.churaibe.global.apiPayload.exception.GeneralException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionAdvice {
    // 커스텀 예외 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<@NonNull ApiResponse<Void>> handleException(GeneralException e) {
        BaseErrorCode code = e.getCode();

        if (code.getHttpStatus().is5xxServerError()) {
            log.error("Internal server error:", e);
        } else {
            log.warn("Client error occurred: {}: {}", code.getCode(), code.getMessage());
        }

        return ResponseEntity
                .status(e.getCode().getHttpStatus())
                .body(ApiResponse.onFailure(
                        e.getCode()
                ));
    }

    // @Valid에서 검증 오류가 발생한 예외에 대한 핸들러
    @ExceptionHandler(BindException.class)
    public ResponseEntity<@NonNull ApiResponse<String>> handleValidationException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        String message = (fieldError != null) ? fieldError.getDefaultMessage() : "검증 오류가 발생했습니다.";
        String fieldName = (fieldError != null) ? fieldError.getField() : "알 수 없는 필드";

        log.warn("Validation error on field '{}': {}", fieldName, message);

        // 400 Bad Request 로 응답
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure(
                        GeneralErrorCode.BAD_REQUEST,
                        String.format("[%s] %s", fieldName, message)
                ));
    }

    // 요청 본문이 없거나 JSON 형식이 깨진 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<@NonNull ApiResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Unreadable request body: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure(
                        GeneralErrorCode.BAD_REQUEST,
                        "요청 본문의 형식이 올바르지 않습니다."
                ));
    }

    // 존재하지 않는 경로 요청 처리
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<@NonNull ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException e) {
        BaseErrorCode code = GeneralErrorCode.NOT_FOUND;
        log.warn("Resource not found: {}", e.getResourcePath());

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ApiResponse.onFailure(code));
    }

    // 그 외의 정의되지 않은 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ApiResponse<String>> handleException(Exception e) {
        BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Internal server error:", e);

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ApiResponse.onFailure(
                        code,
                        "서버 내부 오류가 발생하였습니다."
                ));
    }
}