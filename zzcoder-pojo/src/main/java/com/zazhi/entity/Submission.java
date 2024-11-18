package com.zazhi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author zazhi
 * @date 2024/11/12
 * @description: 提交记录实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    private Long id;
    private Timestamp submitTime;// 提交时间
    private Long userId;// 用户id
    private Integer problemId;// 题目id
    private Integer contestId;// 比赛id
    private String code; // 代码
    private String language;// 语言
    private String status;// 状态 (PENDING JUDGING COMPLETED)
    private String result;// 结果 (ACCEPTED WRONG_ANSWER TIME_LIMIT_EXCEEDED MEMORY_LIMIT_EXCEEDED RUNTIME_ERROR COMPILE_ERROR)
    private String errorMessage;// 错误信息
    private Integer timeUsed;// 时间
    private Integer memoryUsed;// 内存
    private Timestamp createTime;
    private Timestamp updateTime;
}
