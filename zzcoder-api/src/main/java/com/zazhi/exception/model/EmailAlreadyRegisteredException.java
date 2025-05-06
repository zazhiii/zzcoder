package com.zazhi.exception.model;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException() {
        super("邮箱已经注册");
    }

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
