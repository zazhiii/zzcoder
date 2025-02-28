package com.zazhi.judger.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.zazhi.judger.common.exception.SystemException;
import com.zazhi.judger.common.exception.TimeLimitExceededException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.*;

/**
 * @author zazhi
 * @date 2024/12/4
 * @description: Docker工具类
 */
@Component
@Slf4j
@Deprecated
public class DockerUtil {

    @Autowired
    private DockerClient dockerClient;

    /**
     * 创建容器
     *
     * @param tmpDir        代码所在的绝对路径
     * @param memoryLimit   代码允许允许的最大内存
     * @param image         镜像
     * @param containerName 容器名称
     * @return 容器ID
     */
    public String createContainer(String tmpDir, int memoryLimit, String image, String containerName) {
        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .withHostConfig(
                        HostConfig.newHostConfig()
                                .withBinds(new Bind(tmpDir, new Volume("/app"))) // 挂载路径
                                .withMemory(memoryLimit * 1024 * 1024L) // 设置最大内存限制
                                .withMemorySwap(memoryLimit * 1024 * 1024L) // 禁止交换分区 (swap)
                )
                .withName(containerName)
                .withWorkingDir("/app") // 设置工作目录
                .exec();
        log.info("创建容器：{}", container);
        return container.getId();
    }

    /**
     * 启动容器
     *
     * @param containerId
     */
    public void startContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    /**
     * 停止容器
     *
     * @param containerId
     */
    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * 删除容器
     *
     * @param containerId
     */
    public void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }

    /**
     * 创建执行命令
     *
     * @param containerId 容器ID
     * @param cmd         命令
     * @return 执行命令的响应
     */
    public ExecCreateCmdResponse createCmd(String containerId, String... cmd) {
        return dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(cmd)
                .exec();
    }

    /**
     * 执行命令
     *
     * @param execResponse 执行命令的响应
     * @param stdout       标准输出
     * @param stderr       错误输出
     * @throws InterruptedException 中断异常
     */
    public void executeCmd(ExecCreateCmdResponse execResponse, ByteArrayOutputStream stdout, ByteArrayOutputStream stderr) throws InterruptedException {
        dockerClient.execStartCmd(execResponse.getId())
                .exec(new ExecStartResultCallback(stdout, stderr))
                .awaitCompletion();
    }

    /**
     * 超时等待命令执行
     *
     * @param execResponse 创建命令的结果
     * @param timeout      超时时间 (毫秒)
     */
    public void execCmdWithTimeout(ExecCreateCmdResponse execResponse, int timeout, ByteArrayOutputStream stdout, ByteArrayOutputStream stderr) {
        // 使用 ExecutorService 实现超时功能
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            try {
                dockerClient.execStartCmd(execResponse.getId())
                        .exec(new ExecStartResultCallback(stdout, stderr))
                        .awaitCompletion();
            } catch (Exception e) {
                throw new SystemException("Execution failed", e);
            }
        });
        // 设置超时时间
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
            log.info("Command completed within timeout.");
        } catch (TimeoutException e) { // 超时会抛出TimeoutException
            future.cancel(true); // 超时中断任务
            log.error("Command timed out!");
            throw new TimeLimitExceededException();
        } catch (Exception e) {
            log.error("Command execution failed: {}", e.getMessage());
            throw new SystemException("Command execution failed", e);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * 查看容器信息
     *
     * @param containerId
     * @return
     */
    public InspectContainerResponse inspectContainer(String containerId) {
        return dockerClient.inspectContainerCmd(containerId).exec();
    }
}