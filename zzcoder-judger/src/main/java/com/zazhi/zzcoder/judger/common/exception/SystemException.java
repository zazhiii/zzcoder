package com.zazhi.zzcoder.judger.common.exception;

public class SystemException extends RuntimeException {

    public SystemException() {
        super("系统错误");
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
