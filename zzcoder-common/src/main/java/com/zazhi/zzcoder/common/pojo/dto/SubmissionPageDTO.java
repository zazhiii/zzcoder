package com.zazhi.zzcoder.common.pojo.dto;

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
public class SubmissionPageDTO {
    private Integer page;// 当前页码
    private Integer pageSize;// 每页限制数量
    private Integer problemId;// 题目id
    private Integer userId;// 用户id
    private String username;// 用户名
    private String status;// 结果 (AC, WA, TLE, MLE, RE, CE)
    private String language;// 语言
}
