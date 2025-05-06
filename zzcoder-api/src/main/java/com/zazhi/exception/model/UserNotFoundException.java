package com.zazhi.exception.model;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("用户不存在");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
