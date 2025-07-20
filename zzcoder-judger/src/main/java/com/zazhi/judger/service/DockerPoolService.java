package com.zazhi.judger.service;

import com.zazhi.judger.common.pojo.ContainerInfo;

import java.util.List;

/**
 * @author lixh
 * @since 2025/7/20 17:39
 */
public interface DockerPoolService {

    /**
     * 获取所有 Docker 容器的基本信息
     * @return
     */
    List<ContainerInfo> listAllContainers();
}
