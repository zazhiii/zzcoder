package com.zazhi.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author zazhi
 * @date 2025/5/6
 * @description: 比赛列表VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestPageVO {
    private Long id; // 比赛ID
    private String title; // 比赛标题
    private Instant startTime; // 比赛开始时间
    private Instant endTime; // 比赛结束时间
    private Integer duration; // 比赛时长 单位: min
    private Integer status; // 比赛状态
    private Integer type; // 比赛类型
    private String createUserName; // 创建者名字
    private Integer registerCount; // 报名人数
}
