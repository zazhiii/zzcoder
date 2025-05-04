package com.zazhi.enums;

import lombok.Getter;

/**
 * @author zazhi
 * @date 2025/5/4
 * @description: 评测状态
 */

public enum JudgeStatus {
    PENDING("Pending", "等待中"),
    JUDGING("Judging", " 评测中"),
    ACCEPTED("AC", "答案正确"),
    WRONG_ANSWER("WA", "错误答案"),
    TIME_LIMIT_EXCEEDED("TLE", "超出时间限制"),
    MEMORY_LIMIT_EXCEEDED("MLE", "超出内存限制"),
    RUNTIME_ERROR("RE", "运行错误"),
    COMPILE_ERROR("CE", "编译错误"),
    SYSTEM_ERROR("SE", "系统错误");

    @Getter
    private final String name;
    @Getter
    private final String info;

    JudgeStatus(String name, String info) {
        this.name = name;
        this.info = info;
    }

}
