package com.zazhi.judger.common.exception;

public class MemoryLimitExceededException extends JudgeException {
    public MemoryLimitExceededException() {
        super("Memory limit exceeded", 1002);
    }

    public MemoryLimitExceededException(String details) {
        super("Memory limit exceeded", 1002, details);
    }
}
