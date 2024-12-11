package com.tilon.ojt_back.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
