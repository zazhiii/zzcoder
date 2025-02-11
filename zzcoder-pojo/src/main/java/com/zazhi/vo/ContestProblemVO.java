package com.zazhi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zazhi
 * @date 2025/2/11
 * @description: 比赛题目列表VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestProblemVO {
    Integer problemId; // 题目ID
    String title; // 题目标题
    String displayId; // 题目显示ID
}
