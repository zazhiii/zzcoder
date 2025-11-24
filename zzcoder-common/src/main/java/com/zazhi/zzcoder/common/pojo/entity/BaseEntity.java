package com.zazhi.zzcoder.common.pojo.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author lixh
 * @since 2025/8/19 23:38
 */
@Getter
@Setter
public class BaseEntity {
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;
}
