package com.zazhi.judger.common.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
    private Long taskId;  // 任务 ID(submission的id)
    private String status;// 状态
    private String result;// 结果 (AC WA TLE MLE RE CE)
    private String errorMessage;// 错误信息
    private Integer timeUsed;// 时间
    private Integer memoryUsed;// 内存
    private int exitCode;   // 容器退出代码
}
