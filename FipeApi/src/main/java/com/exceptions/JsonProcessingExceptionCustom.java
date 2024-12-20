package com.exceptions;

import lombok.Getter;

@Getter
public class JsonProcessingExceptionCustom extends RuntimeException {

    private final String message;
    private final Throwable cause;

    public JsonProcessingExceptionCustom(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }
}
