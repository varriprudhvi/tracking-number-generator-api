package com.redboxlogistics.tracking.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
