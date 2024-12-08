package com.zazhi.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long createUser;       // 创建人（用户ID）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;  // 创建时间
    private List<ProblemVO> problems; // 题目列表
}
