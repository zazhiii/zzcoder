package com.zazhi.common.pojo.dto;

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
public class ProblemPageDTO implements Serializable {
    private Integer pageSize; // 每页限制数量
    private Integer page; // 当前页码
    private String keyword; // 关键词，用于搜索
    private List<Integer> tagId; // 标签ID列表
    private Integer difficulty; // 题目难度
    private String source; // 题目来源
    private Boolean visible; // 题目是否可见
}
