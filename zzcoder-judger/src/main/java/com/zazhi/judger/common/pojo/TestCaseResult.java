package com.zazhi.judger.common.pojo;

import lombok.Data;

@Data
public class TestCaseResult {
    private int index; // 第几个测试用例
//    private String input;
//    private String expectedOutput;
//    private String actualOutput;
    private long timeUsed; // 单个测试用例耗时
    private long memoryUsed; // 单个测试用例内存
    private String status; // 如 AC, WA, TLE, MLE 等
    private String errorMessage; // 错误信息（如运行时异常）
}
