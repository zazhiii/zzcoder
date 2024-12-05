package com.zazhi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSet {

    private Integer id;            // 主键，自增长
    private String title;          // 题单标题
    private String description;    // 题单描述
    private Integer status;        // 状态（0私有，1公开）
    private Long createUser;       // 创建人（用户ID）
    private Long updateUser;       // 修改人（用户ID）
    private Timestamp createTime;  // 创建时间
    private Timestamp updateTime;  // 更新时间
}
