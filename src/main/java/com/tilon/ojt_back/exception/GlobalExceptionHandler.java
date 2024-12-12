package com.tilon.ojt_back.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException e) {
        e.printStackTrace();
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponseDTO(errorCode));
    }

    // 공통 예외처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception e) {
        e.printStackTrace();
        HttpStatus status;
        ErrorCode errorCode;

        // 400 잘못된 요청
        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            errorCode = ErrorCode.BAD_REQUEST;}
        // 401 인증 정보 유효하지 않음
        else if (e instanceof AuthenticationException) {
            status = HttpStatus.UNAUTHORIZED;
            errorCode = ErrorCode.UNAUTHORIZED;}
        // 403 권한 없음
        else if (e instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            errorCode = ErrorCode.FORBIDDEN;}
        // 404 존재하지 않는 리소스
        else if (e instanceof NoSuchElementException) {
            status = HttpStatus.NOT_FOUND;
            errorCode = ErrorCode.NOT_FOUND;}
        // 500 서버 에러
        else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorCode = ErrorCode.INTERNAL_SERVER_ERROR;}
        // 예외 처리 결과 반환
        return ResponseEntity.status(status).body(new ErrorResponseDTO(errorCode));
    }
}

