package com.zazhi.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemQueryDTO implements Serializable {
    
    // 每页限制数量
    private Integer limit;

    // 当前页码
    private Integer currentPage;

    // 关键词，用于搜索
    private String keyword;

    // 标签ID列表
    private List<Long> tagId;

    // 题目难度
    private Integer difficulty;

    // 题目来源
    private String source;

    // 题目是否可见
    private Boolean problemVisible;
}
