package com.tilon.ojt_back.exception;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private final int status;
    private final String message;

    public ErrorResponseDTO(ErrorCode errorCode) {
        this.status = errorCode.getHttpStatus().value();
        this.message = errorCode.getMessage();
    }
}

