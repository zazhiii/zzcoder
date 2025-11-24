package com.zazhi.zzcoder.common.enums;

import lombok.Getter;

@Getter
public enum EmailCodeBizType {
    REGISTER("register"),
    LOGIN("login"),
    RESET_PASSWORD("reset-password"),
    CHANGE_EMAIL("change-email"),
    UPDATE_EMAIL("update-email");

    private final String code;

    EmailCodeBizType(String code) {
        this.code = code;
    }

    public static boolean isValid(String code) {
        for (EmailCodeBizType type : values()) {
            if (type.getCode().equals(code)) return true;
        }
        return false;
    }

    public static EmailCodeBizType fromCode(String code) {
        for (EmailCodeBizType type : values()) {
            if (type.getCode().equals(code)) return type;
        }
        throw new IllegalArgumentException("Invalid EmailCodeBusinessType code: " + code);
    }
}
