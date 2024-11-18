package com.zazhi.judger.common.exception;

public class TimeLimitExceededException extends JudgeException {

    public TimeLimitExceededException() {
        super("Time limit exceeded", 1001);
    }

    public TimeLimitExceededException(String details) {
        super("Time limit exceeded", 1001, details);
    }
}
