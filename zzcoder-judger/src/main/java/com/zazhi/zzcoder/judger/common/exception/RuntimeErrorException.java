package com.zazhi.zzcoder.judger.common.exception;

public class RuntimeErrorException extends JudgeException {
    public RuntimeErrorException(String details) {
        super("Runtime error", 1003, details);
    }
}
