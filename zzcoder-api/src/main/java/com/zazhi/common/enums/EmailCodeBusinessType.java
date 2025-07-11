package com.zazhi.common.enums;

import lombok.Getter;

@Getter
public enum EmailCodeBusinessType {
    REGISTER("register"),
    LOGIN("login"),
    RESET_PASSWORD("reset_password"),
    CHANGE_EMAIL("change_email");

    private final String code;

    EmailCodeBusinessType(String code) {
        this.code = code;
    }

    public static boolean isValid(String code) {
        for (EmailCodeBusinessType type : values()) {
            if (type.getCode().equals(code)) return true;
        }
        return false;
    }

    public static EmailCodeBusinessType fromCode(String code) {
        for (EmailCodeBusinessType type : values()) {
            if (type.getCode().equals(code)) return type;
        }
        throw new IllegalArgumentException("Invalid EmailCodeBusinessType code: " + code);
    }
}
