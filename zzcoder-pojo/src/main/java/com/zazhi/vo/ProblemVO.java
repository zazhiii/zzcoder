package com.zazhi.vo;

import com.zazhi.entity.Tag;
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
public class ProblemVO implements Serializable {

    private Integer id;

    private String problemId; // 题目编号

    private String title; // 题目标题

    private Integer difficulty; // 难度

//    private Integer type;

    private List<Tag> tags; // 标签
}
