package com.zazhi.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author zazhi
 * @date 2024/11/9
 * @description: 权限类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Integer id;
    private String name;

    private String description;

    private Timestamp createTime; // 创建时间

    private Timestamp updateTime; // 更新时间
}
