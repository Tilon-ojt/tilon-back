package com.tilon.ojt_back.exception;

public class NoUserAuthorizationException extends RuntimeException {
    public NoUserAuthorizationException(String message) {
        super(message);
    }
}
