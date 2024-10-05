package com.zazhi.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("用户名或密码错误");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
