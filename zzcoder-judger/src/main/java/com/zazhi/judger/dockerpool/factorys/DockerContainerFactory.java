package com.zazhi.judger.dockerpool.factorys;


import com.zazhi.judger.dockerpool.containers.DockerContainer;

public interface DockerContainerFactory<T extends DockerContainer> {
    /**
     * 创建一个新的 Docker 容器
     *
     * @param containerName 容器名称
     * @return 新创建的 Docker 容器实例
     */
    T createDockerContainer(String containerName);

    /**
     * 创建一个新的 Docker 容器，使用默认名称
     *
     * @return 新创建的 Docker 容器实例
     */
    T createDockerContainer();
}