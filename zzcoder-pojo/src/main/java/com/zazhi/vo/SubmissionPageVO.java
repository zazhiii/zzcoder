package com.zazhi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author zazhi
 * @date 2024/11/22
 * @description: 提交记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionPageVO {
    private Long id;
    private Timestamp submitTime;// 提交时间
    private Long userId;// 用户id
    private String username;// 用户名
    private Integer problemId;// 题目id
    private String title;// 题目标题
    private String language;// 语言
    private String status;// 状态 (PENDING JUDGING COMPLETED)
    private String result;// 结果 (ACCEPTED WRONG_ANSWER TIME_LIMIT_EXCEEDED MEMORY_LIMIT_EXCEEDED RUNTIME_ERROR COMPILE_ERROR)
    private Integer timeUsed;// 时间
    private Integer memoryUsed;// 内存
}
