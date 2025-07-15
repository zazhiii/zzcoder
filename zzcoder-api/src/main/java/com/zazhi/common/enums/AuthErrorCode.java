package com.zazhi.common.enums;

public enum AuthErrorCode {
    // 通用
    INVALID_REQUEST(400, "非法请求"),
    UNAUTHORIZED(401, "未授权"),

    // 登录
    USER_NOT_FOUND(1001, "用户不存在"),
    PASSWORD_INCORRECT(1002, "用户名或密码错误"),
    ACCOUNT_LOCKED(1003, "账户已锁定"),
    ACCOUNT_DISABLED(1004, "账户已禁用"),

    // 注册
    USERNAME_EXISTS(2001, "用户名已存在"),
    EMAIL_EXISTS(2002, "邮箱已注册"),
    WEAK_PASSWORD(2003, "密码强度不足"),

    // 验证码
    INVALID_BUSINESS(3000, "无效的业务类型"),
    CAPTCHA_INCORRECT(3001, "验证码错误"),
    CAPTCHA_EXPIRED(3002, "验证码已过期");

    private final int code;
    private final String message;

    AuthErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
