package com.zazhi.exception;

public class UsernameAlreadyRegisteredException extends RuntimeException {
    public UsernameAlreadyRegisteredException() {
        super("用户名已经注册");
    }

    public UsernameAlreadyRegisteredException(String message) {
        super(message);
    }
}
