package com.zazhi.judger.common.exception;

public class TimeLimitExceededException extends RuntimeException {

    public TimeLimitExceededException() {
        super("运行超时");
    }

    public TimeLimitExceededException(String details) {
        super("运行超时: " + details);
    }
}
