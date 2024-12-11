package com.tilon.ojt_back.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통 예외처리
    // 400 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // 401 인증 정보 유효하지 않음
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),
    // 403 권한 없음
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    // 404 존재하지 않는 리소스
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리소스입니다."),
    // 409 중복된 리소스
    CONFLICT(HttpStatus.CONFLICT, "중복된 리소스가 존재합니다."),
    // 500 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    
    // post 관련 예외
    INVALID_POST_ID(HttpStatus.BAD_REQUEST, "postId가 유효하지 않습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리가 유효하지 않습니다."),
    INVALID_OFFSET_OR_SIZE(HttpStatus.BAD_REQUEST, "offset 또는 size가 유효하지 않습니다."),
    INVALID_POST_ID_OR_TEMP_POST_ID(HttpStatus.BAD_REQUEST, "postId와 tempPostId 둘 다 없습니다."),
    INVALID_TEMP_POST_ID(HttpStatus.BAD_REQUEST, "tempPostId가 없습니다."),
    INVALID_ADMIN_ID(HttpStatus.BAD_REQUEST, "작성자가 없습니다."),

    // post status, fix 관련 예외
    INVALID_DRAFT_FIX(HttpStatus.BAD_REQUEST, "게시되어 있지 않지만 고정되어 있는 게시물입니다."),
    INVALID_POST_STATUS(HttpStatus.BAD_REQUEST, "고정 게시글은 상태 변경이 불가능합니다."),
    INVALID_POST_FIX(HttpStatus.BAD_REQUEST, "게시글이 게시되어 있을 때만 고정 가능합니다.");
    
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

