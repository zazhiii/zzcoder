package com.zazhi.common.pojo.vo;

import lombok.Data;

import java.sql.Timestamp;

/**
 *
 * @author lixh
 * @since 2025/8/25 23:11
 */
@Data
public class UserProblemSubmissionVO {
    private Long id; // 提交记录id
    private String status;
    private String language;// 语言
    private Integer timeUsed;// 时间
    private Integer memoryUsed;// 内存
    private Timestamp submitTime;// 提交时间
    private String errorMessage;// 错误信息
}
