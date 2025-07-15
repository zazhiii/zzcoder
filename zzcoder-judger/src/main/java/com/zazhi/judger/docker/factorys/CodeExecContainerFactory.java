package com.zazhi.judger.docker.factorys;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.zazhi.judger.docker.containers.CodeExecContainer;

import java.io.File;
import java.util.UUID;

/**
 * @author zazhi
 * @date 2025/7/2
 * @description: CodeExecContainerFactory 类用于创建 CodeExecContainer 实例
 */
public class CodeExecContainerFactory implements DockerContainerFactory<CodeExecContainer> {

    private final DockerClient dockerClient;
    private final String hostWorkingDir;
    private final String containerWorkingDir;
    private final int memoryLimitMb;
    private final String imageName;

    public CodeExecContainerFactory(DockerClient dockerClient,
                                    String hostWorkingDir,
                                    String containerWorkingDir,
                                    int memoryLimitMb,
                                    String imageName) {
        this.dockerClient = dockerClient;
        this.hostWorkingDir = hostWorkingDir;
        this.containerWorkingDir = containerWorkingDir;
        this.memoryLimitMb = memoryLimitMb;
        this.imageName = imageName;
    }

    public CodeExecContainer createDockerContainer(String containerName) {

        String hostWorkingDir = this.hostWorkingDir + File.separator + UUID.randomUUID(); // 使用 UUID 确保每个容器的工作目录唯一

        CreateContainerResponse createRes = dockerClient.createContainerCmd(imageName)
                .withHostConfig(
                        HostConfig.newHostConfig()
                                .withBinds(new Bind(hostWorkingDir, new Volume(containerWorkingDir))) // 挂载路径注意要挂载绝对路径，否则会有点问题
                                .withMemory(memoryLimitMb * 1024 * 1024L) // 设置最大内存限制
                                .withMemorySwap(memoryLimitMb * 1024 * 1024L) // 禁止交换分区 (swap)
                )
                .withName(containerName)
                .withWorkingDir(containerWorkingDir) // 设置工作目录
                .exec();

        return new CodeExecContainer(
                dockerClient,
                createRes.getId(),
                containerName,
                containerWorkingDir,
                hostWorkingDir);
    }

    public CodeExecContainer createDockerContainer() {
        CreateContainerResponse createRes = dockerClient.createContainerCmd(imageName)
                .withHostConfig(
                        HostConfig.newHostConfig()
                                .withBinds(new Bind(hostWorkingDir, new Volume(containerWorkingDir))) // 挂载路径注意要挂载绝对路径，否则会有点问题
                                .withMemory(memoryLimitMb * 1024 * 1024L) // 设置最大内存限制
                                .withMemorySwap(memoryLimitMb * 1024 * 1024L) // 禁止交换分区 (swap)
                )
                .withWorkingDir(containerWorkingDir) // 设置工作目录
                .exec();

        return new CodeExecContainer(dockerClient, createRes.getId(), "",
                containerWorkingDir, hostWorkingDir);
    }

}