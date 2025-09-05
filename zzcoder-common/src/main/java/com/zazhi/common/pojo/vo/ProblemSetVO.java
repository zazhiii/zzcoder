package com.zazhi.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author zazhi
 * @date 2024/12/5
 * @description: 题单详细信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSetVO {
    private Integer id;              // 题单ID
    private String title;          // 题单标题
    private String description;    // 题单描述
    private Integer status;        // 状态（0私有，1公开）
    private Integer createUserId;       // 创建人（用户ID）
    private String createUsername;       // 创建人（用户ID）
    private Timestamp createTime;  // 创建时间
    private List<ProblemSetItemInternalVO> internalProblems; // 内部题目列表
    private List<ProblemSetItemExternalVO> externalProblems; // 外部题目列表
}
