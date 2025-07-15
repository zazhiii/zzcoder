package com.zazhi.judger.common.enums;

import lombok.Getter;

/**
 * @author zazhi
 * @date 2025/5/4
 * @description: 评测状态
 */
@Getter
public enum JudgeStatus {
    PENDING("Pending", "等待中", "正在排队等待评测中，请耐心等待"),
    JUDGING("Judging", " 评测中", "正在评测中, 请稍后查看结果"),
    ACCEPTED("AC", "答案正确", "答案正确, 恭喜通过"),
    WRONG_ANSWER("WA", "错误答案", "答案错误, 请检查代码逻辑"),
    TIME_LIMIT_EXCEEDED("TLE", "超出时间限制", "超出时间限制, 请检查代码的复杂度"),
    MEMORY_LIMIT_EXCEEDED("MLE", "超出内存限制", "超出内存限制, 请检查代码的内存使用情况"),
    RUNTIME_ERROR("RE", "运行错误", "运行时错误, 请检查代码的运行逻辑"),
    COMPILE_ERROR("CE", "编译错误", "编译错误, 请检查代码的语法"),
    SYSTEM_ERROR("SE", "系统错误", "系统错误, 请联系管理员");

    private final String name;
    private final String info;
    private final String msg;

    JudgeStatus(String name, String info, String msg) {
        this.name = name;
        this.info = info;
        this.msg = msg;
    }

}
