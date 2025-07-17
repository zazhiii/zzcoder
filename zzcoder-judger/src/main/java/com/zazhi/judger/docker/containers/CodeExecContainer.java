package com.zazhi.judger.docker.containers;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.zazhi.judger.common.pojo.CodeRunResult;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author zazhi
 * @date 2025/7/1
 * @description: CodeExecContainer class represents a container for executing code.
 */

@Getter
public class CodeExecContainer extends DockerContainer{

    private final String containerWorkingDir;
    private final String hostWorkingDir;

    public CodeExecContainer(DockerClient dockerClient, String containerId, String containerName, String containerWorkingDir, String hostWorkingDir) {
        super(dockerClient, containerId, containerName, System.currentTimeMillis());
        this.containerWorkingDir = containerWorkingDir;
        this.hostWorkingDir = hostWorkingDir;
    }

    /**
     * 编译 Java 代码
     * @param filepath Java 文件路径
     * @return 编译错误信息，如果编译成功则返回空字符串
     */
    public String compileJavaCode(String filepath) {
        ExecCreateCmdResponse resp = this.createCmd("javac", filepath);
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        try {
            this.execCmdAsync(resp, new ByteArrayOutputStream(), stderr, null)
                    .awaitCompletion(10, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("编译 Java 代码时被中断", e);
        }
        return stderr.toString(StandardCharsets.UTF_8);
    }

    /**
     * 执行 Java 代码并计时
     * @param workingDir 容器工作目录
     * @param fileName Java 文件名
     * @param stdin 标准输入流
     * @param timeout 超时时间
     * @param timeunit 超时时间单位
     * @return CodeRunResult 包含执行结果、时间和内存使用情况
     */
    public CodeRunResult runJavaCode(String workingDir,
                                     String fileName,
                                     InputStream stdin,
                                     long timeout,
                                     TimeUnit timeunit
    ) {
        ExecCreateCmdResponse resp = this.createCmd("time", "-f", "__TIME__:%U %S %E %M","java", workingDir + File.separator + fileName);
        boolean awaited = false;
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        try {
            awaited = this.execCmdAsync(resp, stdout, stderr, stdin)
                    .awaitCompletion(timeout, timeunit);
        } catch (InterruptedException e) {
            throw new RuntimeException("执行 Java 代码时被中断", e);
        }
        if(!awaited) {
            return CodeRunResult.timeout();
        }
        String err = stderr.toString(StandardCharsets.UTF_8);
        String output = stdout.toString(StandardCharsets.UTF_8);
        if(err.startsWith("__TIME__:")) {
            String[] parts = err.substring(9).trim().split(" ");
            long timeUsed = (long)Double.parseDouble(parts[2].split(":")[1]) * 1000;
            long memoryUsed = Long.parseLong(parts[3]) / 1024; // MB
            return CodeRunResult.success(output, timeUsed, memoryUsed);
        }
        err = err.split("__TIME__:")[0].trim(); // 只保留时间信息之前的错误信息
        return CodeRunResult.error(err);
    }
}