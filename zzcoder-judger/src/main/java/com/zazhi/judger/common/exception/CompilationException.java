package com.zazhi.judger.common.exception;

public class CompilationException extends RuntimeException {

    public CompilationException() {
        super("编译失败");
    }

    public CompilationException(String message) {
        super(message);
    }
}

