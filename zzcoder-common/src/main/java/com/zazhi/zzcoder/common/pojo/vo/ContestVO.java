package com.zazhi.zzcoder.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestVO {
    private Long id; // 比赛ID
    private String title; // 比赛标题
    private String description; // 比赛描述
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime; // 比赛开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime; // 比赛结束时间
    private Integer duration; // 比赛时长 单位: min
    private Integer status; // 比赛状态
    private Integer type; // 比赛类型
    private String createUserName; // 创建者名字
    private Integer registerCount; // 报名人数
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
