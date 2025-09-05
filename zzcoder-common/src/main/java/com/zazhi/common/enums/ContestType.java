package com.zazhi.common.enums;

import lombok.Getter;

/**
 * @author zazhi
 * @date 2025/5/6
 * @description: 比赛类型枚举类
 */
@Getter
public enum ContestType {
    ACM(0),
    IOI(1),
    OI(2);

    private final Integer code;

    ContestType(Integer code) {
        this.code = code;
    }

    public static ContestType fromCode(int code) {
        for (ContestType types : ContestType.values()) {
            if (types.getCode() == code) {
                return types;
            }
        }
        throw new IllegalArgumentException("Invalid contest type code: " + code);
    }


}
