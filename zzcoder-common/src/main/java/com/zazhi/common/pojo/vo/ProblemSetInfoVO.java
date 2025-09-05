package com.zazhi.common.pojo.vo;

import lombok.Data;

import java.sql.Timestamp;

/**
 *
 * @author lixh
 * @since 2025/8/30 23:43
 */
@Data
public class ProblemSetInfoVO {
    private Integer id;              // 题单ID
    private String title;          // 题单标题
    private String description;    // 题单描述
    private Integer status;         // 题单状态（公开/私有）
    private Integer createUserId;       // 创建人（用户ID）
    private String createUsername;       // 创建人（用户ID）
    private Timestamp createTime;  // 创建时间
}
