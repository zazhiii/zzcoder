package com.zazhi.judger.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author zazhi
 * @date 2024/11/13
 * @description: 判题任务实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeTask {

    // 任务的唯一标识符
    private Long taskId;

    // 题目 ID
    private Integer problemId;

    // 提交用户的 ID
    private Long userId;

    // 编程语言
    private String language;

    // 用户提交的源代码
    private String code;

    // 时间限制（毫秒）
    private Integer timeLimit;

    // 内存限制（MB）
    private Integer memoryLimit;

    // 用户提交代码的时间
    private Timestamp submissionTime;

    // 判题类型（如 ACM 或 OI）
    private String judgeType;

    // 任务的重试次数
    private Integer retryCount;

    // 附加的其他文件
    private List<File> additionalFiles;

    // 隔离配置参数，如内存和 CPU 限制
    private IsolateConfig isolateConfig;

    // 判题完成后回调 URL
    private String callbackUrl;

    // 批量测试用例
    private List<TestCase> testCases;

    // 内嵌的辅助类
    public static class File {
        private String fileName;
        private byte[] fileData;

        // Getters and Setters
    }

    public static class IsolateConfig {
        private Integer cpuLimit;
        private Integer memoryLimit;

        // Getters and Setters
    }
}
