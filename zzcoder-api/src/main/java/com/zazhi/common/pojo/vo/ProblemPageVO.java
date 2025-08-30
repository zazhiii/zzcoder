package com.zazhi.common.pojo.vo;

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
public class ProblemPageVO implements Serializable {
    private Integer id;
    private String problemNumber; // 题目编号
    private String title; // 题目标题
    private Integer difficulty; // 难度
    private List<String> tags; // 标签
}
