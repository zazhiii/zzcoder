package com.zazhi.judger.common.exception;

public class MemoryLimitExceededException extends RuntimeException {
    public MemoryLimitExceededException() {
        super("超出内存限制");
    }

    public MemoryLimitExceededException(String details) {
        super("超出内存限制: " + details);
    }
}
