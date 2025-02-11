package com.zazhi.constant;

public enum ContestStatus {
    NOT_STARTED(0, "未开始"),
    IN_PROGRESS(1, "比赛中"),
    ENDED(2, "已结束");

    private final int code;
    private final String description;

    ContestStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
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
