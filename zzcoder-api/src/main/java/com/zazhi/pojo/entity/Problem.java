package com.zazhi.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem implements Serializable {
    
    private Integer id; // 主键id
    
    private String problemId; // 题目展示id
    
    private String title; // 题目标题
    
    private Integer timeLimit; // 时间限制(ms)
    
    private Integer memoryLimit; // 空间限制(mb)
    
    private Integer stackLimit; // 栈限制(mb)
    
    private String description; // 内容描述
    
    private String inputDescription; // 输入描述
    
    private String outputDescription; // 输出描述

    private String source; // 题目来源，默认为zzcoder
    
    private Integer difficulty; // 题目难度
    
    private String hint; // 备注提醒
    
    private String status; // 权限（0公开，1私有，3比赛中）
    
    private Long createUser; // 创建人
    
    private Long updateUser; // 修改人
    
    private Timestamp createTime; // 创建时间
    
    private Timestamp updateTime; // 更新时间
}
