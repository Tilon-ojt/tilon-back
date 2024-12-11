package com.tilon.ojt_back.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 토큰 관련 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    // 관리자 관련 에러
    INVALID_ADMIN_ID(HttpStatus.BAD_REQUEST, "관리자 번호가 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}

