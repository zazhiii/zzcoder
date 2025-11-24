package com.zazhi.zzcoder.common.pojo.entity;

import com.zazhi.zzcoder.common.enums.JudgeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

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
    private Integer userId;// 用户id
    private Integer problemId;// 题目id
    private Integer contestId;// 比赛id
    private String code; // 代码
    private String language;// 语言
    private JudgeStatus status;// 状态 (PENDING JUDGING AC ... RE CE)
    private List<TestCaseResult> testCaseResults; // 测试用例结果
    private Boolean fullJudge; // 是否全量评测
    private String errorMessage;// 错误信息
    private Long timeUsed;// 时间
    private Long memoryUsed;// 内存
    private Timestamp createTime;
    private Timestamp updateTime;
}
