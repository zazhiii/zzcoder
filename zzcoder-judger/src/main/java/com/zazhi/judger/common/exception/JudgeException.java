package com.zazhi.judger.common.exception;

import lombok.Getter;

@Getter
public class JudgeException extends RuntimeException {
    private final int errorCode; // 自定义错误码
    private String details; // 详细信息

    public JudgeException(String message, int errorCode, String details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public JudgeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
