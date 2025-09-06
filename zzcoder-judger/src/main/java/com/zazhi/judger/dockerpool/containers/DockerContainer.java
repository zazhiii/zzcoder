package com.zazhi.judger.dockerpool.containers;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.zazhi.judger.dockerpool.pojo.CmdExecResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
     * @param cmd 命令
     * @return 执行命令的ID
     */
    public ExecCreateCmdResponse createCmd(String... cmd) {
        return dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withCmd(cmd)
                .exec();
    }

    public CmdExecResult execCmd(String[] cmd) throws InterruptedException {

        String execID = this.createCmd(cmd).getId();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        dockerClient.execStartCmd(execID)
                .exec(new ResultCallback.Adapter<Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        try {
                            if (frame.getStreamType() == StreamType.STDOUT) {
                                out.write(frame.getPayload());
                            } else if (frame.getStreamType() == StreamType.STDERR) {
                                err.write(frame.getPayload());
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error writing to output streams", e);
                        }
                    }
                }).awaitCompletion();

        return CmdExecResult.builder()
                .stdout(out.toString(StandardCharsets.UTF_8))
                .stderr(err.toString(StandardCharsets.UTF_8))
                .build();
    }

    public CmdExecResult execCmd(String[] cmd, int timeout, TimeUnit unit) throws InterruptedException {

        String execID = this.createCmd(cmd).getId();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        dockerClient.execStartCmd(execID)
                .exec(new ResultCallback.Adapter<Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        try {
                            if (frame.getStreamType() == StreamType.STDOUT) {
                                out.write(frame.getPayload());
                            } else if (frame.getStreamType() == StreamType.STDERR) {
                                err.write(frame.getPayload());
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error writing to output streams", e);
                        }
                    }
                }).awaitCompletion(timeout, unit);

        return CmdExecResult.builder()
                .stdout(out.toString(StandardCharsets.UTF_8))
                .stderr(err.toString(StandardCharsets.UTF_8))
                .build();
    }

    public CmdExecResult execCmd(String[] cmd, String stdin, int timeout, TimeUnit unit) throws InterruptedException {

        String execID = this.createCmd(cmd).getId();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        InputStream in = new ByteArrayInputStream(stdin.getBytes());

        boolean awaited = dockerClient.execStartCmd(execID)
                .withStdIn(in)
                .exec(new ResultCallback.Adapter<Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        try {
                            if (frame.getStreamType() == StreamType.STDOUT) {
                                out.write(frame.getPayload());
                            } else if (frame.getStreamType() == StreamType.STDERR) {
                                err.write(frame.getPayload());
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error writing to output streams", e);
                        }
                    }
                }).awaitCompletion(timeout, unit);

        return CmdExecResult.builder()
                .stdout(out.toString())
                .stderr(err.toString())
                .timeout(!awaited)
                .build();
    }

    public CmdExecResult execCmd(String[] cmd, String stdin) throws InterruptedException {
        String execID = this.createCmd(cmd).getId();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        InputStream in = new ByteArrayInputStream(stdin.getBytes());

        dockerClient.execStartCmd(execID)
                .withStdIn(in)
                .exec(new ResultCallback.Adapter<Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        try {
                            if (frame.getStreamType() == StreamType.STDOUT) {
                                out.write(frame.getPayload());
                            } else if (frame.getStreamType() == StreamType.STDERR) {
                                err.write(frame.getPayload());
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error writing to output streams", e);
                        }
                    }
                }).awaitCompletion();

        return CmdExecResult.builder()
                .stdout(out.toString())
                .stderr(err.toString())
                .build();
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

    /**
     * 判断容器是否被OOM杀死
     * @return 是否OOMKilled
     */
    public boolean isOOMKilled() {
        return Boolean.TRUE.equals(inspectContainer().getState().getOOMKilled());
    }
}