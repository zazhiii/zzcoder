package com.zazhi.exception.model;

public class BizException extends RuntimeException {

    private final String message;

    public BizException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
