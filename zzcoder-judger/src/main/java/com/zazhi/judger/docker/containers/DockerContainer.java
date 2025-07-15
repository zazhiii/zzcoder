package com.zazhi.judger.docker.containers;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author zazhi
 * @date 2025/7/1
 * @description: DockerContainer class represents a Docker container in the system.
 */
@Getter
@AllArgsConstructor
public abstract class DockerContainer {

    protected DockerClient dockerClient;

    protected String containerId; // 容器ID

    protected String containerName;

    @Setter
    protected long lastUsedTime; // 最后使用时间

    public void start() {
        dockerClient.startContainerCmd(containerId).exec();
    }

    // 停止容器
    public void stop() {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * 创建执行命令
     * 可以传入标准输入、标准输出和错误输出的流
     *
     * @param cmd 命令
     * @return 执行命令的响应
     */
    public ExecCreateCmdResponse createCmd(String... cmd) {
        return dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withCmd(cmd)
                .exec();
    }

    /**
     * 异步执行命令
     * @param execResponse 创建命令的结果
     * @param stdout 标准输出流
     * @param stderr 标准错误输出流
     * @param stdin 标准输入流
     * @return
     */
    public ResultCallback.Adapter<Frame> execCmdAsync(ExecCreateCmdResponse execResponse,
                                                ByteArrayOutputStream stdout,
                                                ByteArrayOutputStream stderr,
                                                      InputStream stdin) {
        return dockerClient.execStartCmd(execResponse.getId())
                .withStdIn(stdin)
                .exec(new ResultCallback.Adapter<Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        try {
                            if (frame.getStreamType() == StreamType.STDOUT) {
                                stdout.write(frame.getPayload());
                            } else if (frame.getStreamType() == StreamType.STDERR) {
                                stderr.write(frame.getPayload());
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error writing to output streams", e);
                        }
                    }
                });
    }

    /**
     * 执行命令
     *
     * @param execResponse 创建命令的结果
     * @param stdout       标准输出流
     * @param stderr       标准错误输出流
     * @param stdin        标准输入流
     * @throws InterruptedException 如果执行过程中被中断
     */
    public void execCmd(ExecCreateCmdResponse execResponse,
                        ByteArrayOutputStream stdout,
                        ByteArrayOutputStream stderr,
                        InputStream stdin
    ) throws InterruptedException {
        this.execCmdAsync(execResponse, stdout, stderr, stdin)
                .awaitCompletion();
    }

    /**
     * 超时等待命令执行
     * @param execResponse 创建命令的结果
     * @param stdout       标准输出流
     * @param stderr       标准错误输出流
     * @param stdin        标准输入流
     * @param timeout      超时时间
     * @param timeUnit  超时时间单位
     * @return 是否在超时时间内成功执行
     * @throws InterruptedException 如果执行过程中被中断
     */
    public boolean execCmdWithTimeout(
            ExecCreateCmdResponse execResponse,
            ByteArrayOutputStream stdout,
            ByteArrayOutputStream stderr,
            InputStream stdin,
            int timeout,
            TimeUnit timeUnit
            ) throws InterruptedException {

        return this.execCmdAsync(execResponse, stdout, stderr, stdin)
                .awaitCompletion(timeout, timeUnit);
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