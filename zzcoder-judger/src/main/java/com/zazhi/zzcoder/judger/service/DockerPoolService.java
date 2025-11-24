package com.zazhi.zzcoder.judger.service;

import com.zazhi.zzcoder.common.pojo.vo.DockerContainerInfoVO;
import com.zazhi.zzcoder.judger.common.pojo.ContainerInfo;

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

    /**
     * 获取所有 Docker 容器的详细信息
     * @return
     */
    List<DockerContainerInfoVO> list();
}
