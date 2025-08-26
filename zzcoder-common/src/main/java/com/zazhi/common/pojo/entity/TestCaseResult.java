package com.zazhi.common.pojo.entity;

import com.zazhi.common.enums.JudgeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseResult {
    private int index; // 第几个测试用例
    private Long testCaseId; // 测试用例ID
    private Long submissionId; // 提交ID
    private Long timeUsed; // 单个测试用例耗时
    private Long memoryUsed; // 单个测试用例内存
    private JudgeStatus status; // 如 AC, WA, TLE, MLE 等
    private String errorMessage; // 错误信息（如运行时异常）
}
