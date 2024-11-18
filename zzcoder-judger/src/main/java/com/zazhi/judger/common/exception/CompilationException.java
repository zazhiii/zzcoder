package com.zazhi.judger.common.exception;

public class CompilationException extends RuntimeException {

    public CompilationException() {
        super("Compilation failed");
    }

    public CompilationException(String message) {
        super(message);
    }
}
