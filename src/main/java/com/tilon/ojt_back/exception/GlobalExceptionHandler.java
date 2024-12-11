package com.tilon.ojt_back.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(ResponseStatusException e) {
        ErrorCode errorCode = ErrorCode.valueOf(e.getStatusCode().toString());
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponseDTO(errorCode));
    }
}
