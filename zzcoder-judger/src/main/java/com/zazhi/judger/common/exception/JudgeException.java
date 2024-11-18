package com.zazhi.judger.common.exception;

public class JudgeException extends RuntimeException {
    private int errorCode; // 自定义错误码
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

    public int getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }
}
