package com.zazhi.judger.common.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
    private Long taskId;  // 任务 ID(submission的id)
    private String status;// 状态(PENDING, JUDGING, AC WA TLE MLE RE CE)
    private List<TestCaseResult> testCaseResults; // 测试用例结果
    private Boolean fullJudge; // 是否全量评测
    private String errorMessage; // 错误信息
    private Long timeUsed = 0L; // 时间
    private Long memoryUsed = 0L; // 内存
//    private String result;
//    private int exitCode; // 容器退出代码

    public static JudgeResult compileError(String errorMessage) {
        return JudgeResult.builder()
                .status("CE")
                .errorMessage(errorMessage)
                .build();
    }
}
