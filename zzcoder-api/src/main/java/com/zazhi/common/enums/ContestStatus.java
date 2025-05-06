package com.zazhi.common.enums;

import lombok.Getter;

@Getter
public enum ContestStatus {
    UPCOMING(0, "未开始"),
    ACTIVE(1, "比赛中"),
    ENDED(2, "已结束");

    private final int code;
    private final String description;

    ContestStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ContestStatus fromCode(int code) {
        for (ContestStatus status : ContestStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid contest status code: " + code);
    }
}
