package com.zazhi.judger.common.pojo;

import com.zazhi.judger.common.enums.JudgeStatus;
import lombok.Data;

@Data
public class TestCaseResult {
    private int index; // 第几个测试用例
//    private String input;
//    private String expectedOutput;
//    private String actualOutput;
    private Integer timeUsed; // 单个测试用例耗时
    private Double memoryUsed; // 单个测试用例内存
    private JudgeStatus status; // 如 AC, WA, TLE, MLE 等
    private String errorMessage; // 错误信息（如运行时异常）
}
