package com.zazhi.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestDTO {
    private Long id; // 比赛id
    private String title; // 比赛标题
    private String description; // 比赛描述
    private Instant startTime; // 比赛开始时间
    private Instant endTime; // 比赛结束时间
    private Integer duration; // 比赛时长
    private Integer visible; // 公开状态
    private Integer type; // 比赛类型
    private String password; // 比赛密码
}
