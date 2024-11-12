package com.zazhi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zazhi
 * @date 2024/11/12
 * @description: 提交记录查询DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionQueryDTO {
    private Integer limit;// 每页限制数量
    private Integer currentPage;// 当前页码
    private String username;// 用户名
    private String result;// 结果 (AC, WA, TLE, MLE, RE, CE)
    private String language;// 语言
    private Integer contestId;// 比赛id
//    private String problemTitle;// 题目标题
}
