package com.zazhi.judger.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.zazhi.judger.common.exception.SystemException;
import com.zazhi.judger.common.exception.TimeLimitExceededException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.*;

@Data
@Slf4j
public class DockerContainer {

    // Docker 客户端
    private DockerClient dockerClient;

    // 假设 Docker 容器有一些状态，实际可以根据需要扩展
    private boolean isBusy = false;

    // Docker 容器 ID
    private String containerId;

    private String containerName;

    // Docker 容器工作目录
    private String workingDir;


    public DockerContainer(DockerClient dockerClient, String containerId, String containerName, String workingDir) {
        this.dockerClient = dockerClient;
        this.containerId = containerId;
        this.containerName = containerName;
        this.workingDir = workingDir;
    }

    /**
     * 启动容器
     */
    public void start() {
        dockerClient.startContainerCmd(containerId).exec();
    }

    // 停止容器
    public void stop() {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * 创建执行命令
     *
     * @param cmd         命令
     * @return 执行命令的响应
     */
    public ExecCreateCmdResponse createCmd(String... cmd) {
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
     * @return 容器信息
     */
    public InspectContainerResponse inspectContainer() {
        return dockerClient.inspectContainerCmd(containerId).exec();
    }

    /**
     * 判断容器是否在运行
     *
     * @return 是否在运行
     */
    public boolean isRunning() {
        return Boolean.TRUE.equals(inspectContainer().getState().getRunning());
    }
}
