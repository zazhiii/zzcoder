package com.zazhi.exception.model;

public class VerificationCodeException extends RuntimeException {
    public VerificationCodeException() {
        super("验证码过期或错误");
    }

    public VerificationCodeException(String message) {
        super(message);
    }
}
