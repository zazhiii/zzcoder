package com.zazhi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    private Long id; // 比赛ID
    private String title; // 比赛标题
    private String description; // 比赛描述
    private LocalDateTime startTime; // 比赛开始时间
    private LocalDateTime endTime; // 比赛结束时间
    private Integer status; // 比赛状态
    private Integer visible; // 公开状态 0公开 1私有（密码）
    private Integer type; // 比赛类型
    private String password; // 比赛密码
    private Long createUser; // 创建者
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
