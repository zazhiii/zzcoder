package com.zazhi.judger.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.zazhi.judger.common.exception.*;
import com.zazhi.judger.common.pojo.ExecutionStats;
import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.common.pojo.TestCase;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author zazhi
 * @date 2024/11/16
 * @description: docker 工具类
 */

@Service
public class JudgeService {

    private static final Logger log = LoggerFactory.getLogger(JudgeService.class);
    @Autowired
    DockerClient dockerClient;

    /**
     * 创建容器并挂载目录
     *
     * @param tmpDir 宿主机的临时目录
     */
    public String createContainer(String tmpDir, int memoryLimit) {
        // TODO : 这里只支持 Java 语言，需要根据不同语言编译
        CreateContainerResponse container = dockerClient.createContainerCmd("java_judger")
                .withHostConfig(
                        HostConfig.newHostConfig()
                                .withBinds(new Bind(tmpDir, new Volume("/app"))) // 挂载路径
                                .withMemory(memoryLimit * 1024 * 1024L) // 设置最大内存限制
                                .withMemorySwap(memoryLimit * 1024 * 1024L) // 禁止交换分区 (swap)
                )
                .withWorkingDir("/app") // 设置工作目录
                .exec();
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
     * 删除容器
     *
     * @param containerId
     */
    public void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
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
     * 在指定容器中编译代码
     */
    public void compileCode(String containerId) {
        // 创建命令
        // TODO : 这里只支持 Java 语言，需要根据不同语言编译
        ExecCreateCmdResponse execResponse = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(
                        "/bin/sh", "-c",
                        "javac Main.java"
                )
                .exec();
        // 执行命令
        // 捕获标准输出和错误输出
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        try {
            dockerClient.execStartCmd(execResponse.getId())
                    .exec(new ExecStartResultCallback(stdout, stderr))
                    .awaitCompletion();

            // 获取输出结果
            String stdoutResult = stdout.toString(StandardCharsets.UTF_8).trim();
            String stderrResult = stderr.toString(StandardCharsets.UTF_8).trim();

            // 处理编译结果
            if (!stderrResult.isEmpty()) {
                // 编译有错误
                System.err.println("Compilation errors:" + stderrResult);
                throw new CompilationException("Compilation failed:" + stderrResult);
            } else if (!stdoutResult.isEmpty()) {
                // 编译成功的信息（如果需要可以打印）
                System.out.println("Compilation succeeded:" + stdoutResult);
            } else {
                System.out.println("Compilation completed without any output.");
            }
        } catch (InterruptedException | DockerException e) { // 这里算Judge系统错误
            throw new SystemException("Compilation process failed", e);
        }
    }


    /**
     * 解析 time 命令的输出
     *
     * @param timeOutput
     * @return
     */
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

    /**
     * 在指定容器中运行代码
     */
    public void runCode(int timeLimit, String containerId) {
        ExecCreateCmdResponse execResponse = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(
                        "/usr/bin/time", "-v", "-o", "/app/time_output.txt",
                        "/bin/sh", "-c",
                        "java Main < /app/input.txt > /app/output.txt"
                )
                .exec();
        // 捕获输出
        ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        ByteArrayOutputStream stdErr = new ByteArrayOutputStream();

        // 执行命令
        execCmdWithTimeout(execResponse, new ExecStartResultCallback(stdOut, stdErr), timeLimit);

        // 输出结果 解析时间和内存 使用 /usr/bin/time -v 命令(需要安装time, 信息默认输出到stderr)
        String executionOutput = stdOut.toString(StandardCharsets.UTF_8);
        String errorOutput = stdErr.toString(StandardCharsets.UTF_8);

        log.info("输出：{}", executionOutput);
        log.error("错误：{}", errorOutput);

        if (!errorOutput.isEmpty()) {
            log.error("运行时错误: {}", errorOutput);
            throw new RuntimeErrorException(errorOutput);
        }

        // 解析运行情况的: =====
//        String timeOutput = null;
//        try {
//            timeOutput = FileUtils.readFileToString(new File("/app/time_output.txt"), "UTF-8");
//        } catch (IOException e) {
//            throw new SystemException("读取时间输出文件失败", e);
//        }
//        ExecutionStats executionStats = parseTimeOutput(timeOutput);
        // 如果wait返回的是-1则代表超时, 返回运行时间为-1代表超时
        // 如果wait返回的是-2则代表失败, 返回运行时间为-2代表失败
//        return executionStats;
    }


    /**
     * 超时等待命令执行
     *
     * @param execResponse 创建命令的结果
     * @param callback     回调
     * @param timeout      超时时间 (毫秒)
     */
    private void execCmdWithTimeout(ExecCreateCmdResponse execResponse, ExecStartResultCallback callback, int timeout) {
        // 使用 ExecutorService 实现超时功能
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            try {
                dockerClient.execStartCmd(execResponse.getId()).exec(callback).awaitCompletion();
            } catch (Exception e) {
                throw new SystemException("Execution failed", e);
            }
        });
        // 设置超时时间
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
            System.out.println("Command completed within timeout.");
        } catch (TimeoutException e) { // 超时会抛出TimeoutException
            future.cancel(true); // 超时中断任务
            System.err.println("Command timed out!");
            throw new TimeLimitExceededException();
        } catch (Exception e) {
            System.err.println("Command execution failed: " + e.getMessage());
            throw new SystemException("Command execution failed", e);
        } finally {
            executor.shutdownNow();
        }
    }


    /**
     * 处理任务
     *
     * @param task
     * @param judgeResult
     * @return
     */
    public JudgeResult processTask(JudgeTask task, JudgeResult judgeResult) {
        String containerId = null;
        judgeResult.setTimeUsed(0);
        judgeResult.setMemoryUsed(0);
        try {
            // 创建临时目录用于存放用户提交的代码和文件
            String tempDir = "/tmp/judgeTask_" + task.getTaskId();
            File tempDirectory = new File(tempDir);
            tempDirectory.mkdir();
            // 将用户的代码保存到文件中
            File codeFile = new File(tempDir, "Main.java");
            FileUtils.writeStringToFile(codeFile, task.getCode(), "UTF-8");
            // 宿主机的临时目录绝对路径
            String tmpDirAbsPath = tempDirectory.getAbsolutePath();
            // 创建 Docker 容器, 并挂载目录, 设置内存限制
            containerId = createContainer(tmpDirAbsPath, task.getMemoryLimit());
            // 启动容器
            startContainer(containerId);
            // 编译用户的代码
            compileCode(containerId);
            // 获取测试用例
            List<TestCase> testCases = task.getTestCases();
            for (TestCase testCase : testCases) {
                log.info("测试用例: {}", testCase);
                // 将测试用例保存到文件中
                File inputFile = new File(tempDir, "input.txt");
                File expectedOutput = new File(tempDir, "expected_output.txt");
                FileUtils.writeStringToFile(inputFile, testCase.getInput(), "UTF-8");
                FileUtils.writeStringToFile(expectedOutput, testCase.getExpectedOutput(), "UTF-8");
                // 监测容器状态
                InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerId).exec();
                // 根据编程语言和任务配置创建 Docker 容器并运行代码
                runCode(task.getTimeLimit(), containerId);
                // 判断是否超内存
                boolean oomKilled = containerResponse.getState().getOOMKilled();
                if (oomKilled) {
                    throw new MemoryLimitExceededException();
                }

                // 从time_output.txt解析运行情况
                String timeOutput = null;
                try {
                    timeOutput = FileUtils.readFileToString(new File(tmpDirAbsPath + "/time_output.txt"), "UTF-8");
                } catch (IOException e) {
                    throw new SystemException("读取时间输出文件失败", e);
                }
                ExecutionStats executionStats = parseTimeOutput(timeOutput);

                // 更新评测结果 (时间和内存的最大值)
                if (executionStats.getExecutionTime() > judgeResult.getTimeUsed()) {
                    judgeResult.setTimeUsed(executionStats.getExecutionTime());
                }
                if (executionStats.getMemoryUsage() > judgeResult.getMemoryUsed()) {
                    judgeResult.setMemoryUsed(executionStats.getMemoryUsage());
                }

                // 判断输出是否正确(output.txt 和 expected_output.txt)
                File output = new File(tmpDirAbsPath + "/output.txt");
                String outputContent = FileUtils.readFileToString(output, "UTF-8");
                String expectedOutputContent = FileUtils.readFileToString(expectedOutput, "UTF-8");
                if (!outputContent.trim().equals(expectedOutputContent.trim())) {
                    log.info("样例失败{} -- but expected: {}", outputContent, expectedOutputContent);
                    throw new WrongAnswerException("Wrong Answer on test case: "); //TODO 第几个测试用例
                }
            }
            judgeResult.setResult("AC");
        } catch (SystemException | IOException e) {
            judgeResult.setResult("SE");
            judgeResult.setErrorMessage(e.getMessage());
        } catch (RuntimeErrorException e) {
            judgeResult.setResult("RE");
            judgeResult.setErrorMessage(e.getDetails());
        } catch (CompilationException e) {
            judgeResult.setResult("CE");
            judgeResult.setErrorMessage(e.getMessage());
        } catch (TimeLimitExceededException e) {
            judgeResult.setResult("TLE");
            judgeResult.setErrorMessage(e.getDetails());
        } catch (MemoryLimitExceededException e) {
            judgeResult.setResult("MLE");
            judgeResult.setErrorMessage(e.getDetails());
        } catch (WrongAnswerException e) {
            judgeResult.setResult("WA");
            judgeResult.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            judgeResult.setResult("SE");
            judgeResult.setErrorMessage("未知错误");
        } finally {
            stopContainer(containerId);
            removeContainer(containerId);
            FileUtils.deleteQuietly(new File("/tmp/judgeTask_" + task.getTaskId()));
            judgeResult.setStatus("finished");
        }
        return judgeResult;
    }
}
