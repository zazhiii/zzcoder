package com.zazhi.exception.model;

import com.zazhi.common.enums.AuthErrorCode;

public class AuthException extends BaseException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.code(), errorCode.message());
    }

    public AuthException(AuthErrorCode errorCode, String customMessage) {
        super(errorCode.code(), customMessage);
    }
}
