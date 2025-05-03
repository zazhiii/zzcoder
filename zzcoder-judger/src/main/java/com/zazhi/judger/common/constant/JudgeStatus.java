package com.zazhi.judger.common.constant;

/**
 * @author zazhi
 * @date 2025/5/2
 * @description: 评测状态
 */
public class JudgeStatus {
    public static final String PENDING = "Pending"; // 等待中
    public static final String JUDGING = "Judging"; // 评测中
    public static final String ACCEPTED = "AC"; // 正确
    public static final String WRONG_ANSWER = "WA"; // 错误答案
    public static final String TIME_LIMIT_EXCEEDED = "TLE"; // 超时
    public static final String MEMORY_LIMIT_EXCEEDED = "MLE"; // 内存超限
    public static final String RUNTIME_ERROR = "RE"; // 运行错误
    public static final String COMPILE_ERROR = "CE"; // 编译错误
    public static final String SYSTEM_ERROR = "SE"; // 系统错误
}
