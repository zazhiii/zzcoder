package com.zazhi.judger.common.exception;

public class SystemException extends RuntimeException {

    public SystemException() {
        super("System error");
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
