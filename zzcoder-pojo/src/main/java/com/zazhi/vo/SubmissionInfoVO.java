package com.zazhi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author zazhi
 * @date 2024/11/20
 * @description: 提交记录详情VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionInfoVO {
    private Long id; // 提交记录id
    private Timestamp submitTime;// 提交时间
    private Long userId;// 用户id
    private String username;// 用户名
    private Integer problemId;// 题目id
    private String title;// 题目标题
    private String contestId;// 比赛编号
    private String code; // 代码
    private String language;// 语言
    private String status;// 状态 (PENDING JUDGING COMPLETED)
    private String result;// 结果 (ACCEPTED WRONG_ANSWER TIME_LIMIT_EXCEEDED MEMORY_LIMIT_EXCEEDED RUNTIME_ERROR COMPILE_ERROR)
    private String errorMessage;// 错误信息
    private Integer timeUsed;// 时间
    private Integer memoryUsed;// 内存
}
