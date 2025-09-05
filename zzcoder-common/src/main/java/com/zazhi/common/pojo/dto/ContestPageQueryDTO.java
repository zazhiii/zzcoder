package com.zazhi.common.pojo.dto;

import com.zazhi.common.enums.ContestStatus;
import com.zazhi.common.enums.ContestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zazhi
 * @date 2025/5/6
 * @description: 比赛分页查询DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestPageQueryDTO implements Serializable {

    private Integer pageSize; // 每页大小
    private Integer pageNum; // 当前页码

    private String title; // 比赛标题
    private ContestStatus status; // 比赛状态
    private ContestType type; // 比赛类型

}
