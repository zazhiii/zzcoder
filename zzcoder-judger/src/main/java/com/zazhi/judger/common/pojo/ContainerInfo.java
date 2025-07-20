package com.zazhi.judger.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zazhi
 * @date 2025/7/20
 * @description: ContainerInfo 类表示 Docker 容器的基本信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerInfo {
    private String id;
    private String name;
    private String image;
    private String status; // "idle", "running", "error"
    private String createdTime;
    private String usedBy;
    private String state; // Docker 容器的实际状态
    private Long memoryLimit;
    private Long memoryUsage;
    private String ports;

    // 简化构造函数，只包含基本信息
    public ContainerInfo(String id, String name, String image, String status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
    }
}
