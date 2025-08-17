package com.zazhi.common.pojo.entity;


import com.zazhi.common.enums.JudgeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
    private Long taskId;  // 任务 ID(submission的id)
    private JudgeStatus status;// 状态(PENDING, JUDGING, AC WA TLE MLE RE CE)
    private List<TestCaseResult> testCaseResults = new ArrayList<>(); // 测试用例结果
    private Boolean fullJudge; // 是否全量评测
    private String errorMessage; // 错误信息
    private Long timeUsed = 0L; // 时间
    private Long memoryUsed = 0L; // 内存
}
