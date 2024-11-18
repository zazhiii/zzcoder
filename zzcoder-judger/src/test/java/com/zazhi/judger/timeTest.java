package com.zazhi.judger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.zazhi.judger.common.pojo.ExecutionStats;
import com.zazhi.judger.service.JudgeService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * @author zazhi
 * @date 2024/11/16
 * @description: TODO
 */
//@SpringBootTest
public class timeTest {

    private static final Logger log = LoggerFactory.getLogger(timeTest.class);
    @Autowired
    DockerClient dockerClient;
    @Autowired
    JudgeService judgeService;

    @Test
    public void removeContainer() {
        dockerClient.removeContainerCmd("d066cd4aef5af66dc98865741f1736daaf3415720cf439effe3bbb02ce51fe10").exec();
    }

    @Test
    public void time() throws IOException, InterruptedException {


        // 0. 创建容器并挂载文件目录
        String codePath = "C:/tmp/judgeTask_47";
        CreateContainerResponse container = dockerClient.createContainerCmd("java_judger")
                .withHostConfig(
                        HostConfig.newHostConfig()
                                .withBinds(new Bind(codePath, new Volume("/app"))) // Mount path
                )
                .withWorkingDir("/app") // Set working directory
                .exec();

        // 1. 启动容器
        dockerClient.startContainerCmd(container.getId()).exec();

//        judgeService.compileCode(codePath, container.getId());

        // /bin/sh -c : 使用 sh 作为子解释器来执行命令
        // java Main < /app/input.txt > /app/output.txt作为整个命令 : 确保重定向符号 (< 和 >) 被 sh 正确处理，而不是被 time 直接解析。
        // 否则会因为重定向符号无法被time解析而导致 time 报错。
        // 创建 Exec 命令 (这个是在运行的容器内执行的命令)
        ExecCreateCmdResponse execResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(
                        "/usr/bin/time", "-v",
                        "/bin/sh", "-c",
                        "java Main < /app/input.txt > /app/output.txt"
                )
                .exec();

        // 创建输出流
        ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        ByteArrayOutputStream stdErr = new ByteArrayOutputStream();

        int wait = waitForCmdWithTimeout(execResponse, new ExecStartResultCallback(stdOut, stdErr), 1);
        log.info("wait: {}", wait);

//        // 执行并等待结果
//        try {
//            // 创建回调
//            ExecStartResultCallback callback = new ExecStartResultCallback(stdOut, stdErr);
//
//            // 使用 ExecutorService 实现超时功能
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            Future<?> future = executor.submit(() -> {
//                try {
//                    dockerClient.execStartCmd(execResponse.getId()).exec(callback).awaitCompletion();
//                } catch (Exception e) {
//                    throw new RuntimeException("Execution failed", e);
//                }
//            });
//
//            // 设置超时时间
//            try {
//                future.get(1, TimeUnit.SECONDS);
//                System.out.println("Command completed within timeout.");
//            } catch (TimeoutException e) {
//                future.cancel(true); // 超时中断任务
//                System.err.println("Command timed out!");
//            } catch (Exception e) {
//                System.err.println("Command execution failed: " + e.getMessage());
//            } finally {
//                executor.shutdownNow();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        // 输出结果
        String executionOutput = stdOut.toString(StandardCharsets.UTF_8);
        String errorOutput = stdErr.toString(StandardCharsets.UTF_8);
        log.info("输出：{}", executionOutput);
        log.info("错误：{}", errorOutput);
        ExecutionStats executionStats = parseTimeOutput(errorOutput);
        log.info("TIME: {}ms", executionStats.getExecutionTime());
        log.info("MEMORY: {}kb", executionStats.getMemoryUsage());

    }

    private int waitForCmdWithTimeout(ExecCreateCmdResponse execResponse, ExecStartResultCallback callback, int timeout) {
        try {
            // 使用 ExecutorService 实现超时功能
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(() -> {
                try {
                    dockerClient.execStartCmd(execResponse.getId()).exec(callback).awaitCompletion();
                } catch (Exception e) {
                    throw new RuntimeException("Execution failed", e);
                }
            });
            // 设置超时时间
            try {
                future.get(timeout, TimeUnit.SECONDS);
                System.out.println("Command completed within timeout.");
                return 0;
            } catch (TimeoutException e) {
                future.cancel(true); // 超时中断任务
                System.err.println("Command timed out!");
                return -1;
            } catch (Exception e) {
                System.err.println("Command execution failed: " + e.getMessage());
                return -2;
            } finally {
                executor.shutdownNow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    private static ExecutionStats parseTimeOutput(String timeOutput) {
        ExecutionStats executionStats = new ExecutionStats();
        String[] lines = timeOutput.split("\n");
        int executionTime = 0;
        int memoryUsage = 0;
        for (String line : lines) {
            if (line.contains("Elapsed (wall clock) time")) {
                executionTime = (int) (Double.parseDouble(line.split(": ")[1].split(":")[1]) * 1000);
            } else if (line.contains("Maximum resident set size")) {
                memoryUsage = Integer.parseInt(line.split(": ")[1]);
            }
        }
        return new ExecutionStats(executionTime, memoryUsage);
    }


    @Test
    public void exception() {
        try{
            throw new RuntimeException("程序执行异常");
        } catch (RuntimeException e) {
            log.error("程序执行异常");
        }
    }
}
