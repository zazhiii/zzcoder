package com.zazhi.zzcoder.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author lixh
 * @since 2025/8/31 14:03
 */
@Getter
@AllArgsConstructor
public enum AuthError implements IErrorCode {
    AUTH_INVALID_CREDENTIALS(10001, "无效的认证凭据"),
    AUTH_TOKEN_EXPIRED(10002, "认证令牌已过期"),
    AUTH_INSUFFICIENT_PERMISSIONS(10003, "权限不足"),
    AUTH_USER_NOT_FOUND(10004, "用户未找到"),
    AUTH_ACCOUNT_LOCKED(10005, "账户已锁定"),
    AUTH_ACCOUNT_DISABLED(10006, "账户已禁用"),
    AUTH_PASSWORD_EXPIRED(10007, "密码已过期"),
    AUTH_INVALID_TOKEN(10008, "无效的令牌"),
    AUTH_UNAUTHORIZED_ACCESS(10009, "未授权的访问"),
    AUTH_SESSION_EXPIRED(10010, "会话已过期"),
    ORIGINAL_PASSWORD_INCORRECT(10011, "原始密码不正确"),
    ROLE_EXISTS(10012, "角色已存在"),
    INVALID_BUSINESS_TYPE(10013, "无效的业务类型"),
    EMAIL_SEND_FAIL(10014, "邮件发送失败"),
    CAPTCHA_INCORRECT(10015, "验证码错误"),
    CAPTCHA_EXPIRED(10016, "验证码已过期"),
    EMAIL_CODE_INCORRECT_OR_EXPIRED(10017, "邮箱验证码错误或已过期");

    private final int code;
    private final String message;
}
