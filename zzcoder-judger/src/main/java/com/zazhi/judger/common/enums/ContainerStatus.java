package com.zazhi.judger.common.enums;

import lombok.Getter;

/**
 * @author lixh
 * @since 2025/8/18 21:27
 */
@Getter
public enum ContainerStatus {
    /**
     * 容器处于空闲状态，可接受新任务
     */
    IDLE("空闲"),

    /**
     * 容器正在处理任务，处于繁忙状态
     */
    BUSY("繁忙"),

    /**
     * 容器已停止运行
     */
    STOPPED("停止"),

    /**
     * 容器出现异常，无法正常工作
     */
    ERROR("异常");

    private final String description;

    ContainerStatus(String description) {
        this.description = description;
    }
}
