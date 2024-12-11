package com.tilon.ojt_back.exception;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private final int status;
    private final String message;

    public ErrorResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

