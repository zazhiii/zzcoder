package com.zazhi.judger.service;

import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.zazhi.judger.common.exception.*;
import com.zazhi.judger.common.pojo.ExecutionStats;
import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.common.pojo.TestCase;
import com.zazhi.judger.common.utils.MessageQueueUtil;
import com.zazhi.judger.docker.DockerContainer;
import com.zazhi.judger.docker.DockerContainerPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zazhi
 * @date 2024/12/4
 * @description: Java沙箱
 */
@Service
@Slf4j
public class JavaSandBox {

    @Autowired
    private MessageQueueUtil messageQueueUtil;

    @Autowired
    private DockerContainerPool dockerContainerPool;

    /**
     * ========================================================
     * ================== 以下为沙箱的核心方法 ===================
     * ========================================================
     *
     * @param task   任务
     */
    public void processTask(JudgeTask task) {
        log.info("开始处理评测任务: {}", task);

        // 0. 创建评测结果对象
        JudgeResult result = new JudgeResult();
        result.setTaskId(task.getTaskId());
        result.setStatus("Judging"); // TODO 抽取常量
        messageQueueUtil.sendJudgeResult(result); // 更新状态

        DockerContainer container = null;
        try {
            // 获取一个空闲的容器
            container = dockerContainerPool.acquireContainer();
            // 工作目录
            String workingDir = container.getWorkingDir();
            File workingDirectory = new File(workingDir);
            // 宿主机的工作目录绝对路径
            String workingDirectoryAbsolutePath = workingDirectory.getAbsolutePath();
            // 将用户的代码保存到文件中
            File codeFile = new File(workingDirectoryAbsolutePath, "Main.java"); // TODO 抽取常量
            FileUtils.writeStringToFile(codeFile, task.getCode(), "UTF-8");
            // 编译用户的代码
            compileCode(container, "/bin/sh", "-c", "javac Main.java"); // TODO 抽取常量
            // 获取测试用例
            List<TestCase> testCases = task.getTestCases();
            for (TestCase testCase : testCases) {
                log.info("测试用例: {}", testCase);
                // 将测试用例保存到文件中
                File inputFile = new File(workingDirectoryAbsolutePath, "input.txt");
                File expectedOutput = new File(workingDirectoryAbsolutePath, "expected_output.txt");
                FileUtils.writeStringToFile(inputFile, testCase.getInput(), "UTF-8");
                FileUtils.writeStringToFile(expectedOutput, testCase.getExpectedOutput(), "UTF-8");
                // 根据编程语言和任务配置创建 Docker 容器并运行代码
                String[] cmd = {"/usr/bin/time", "-v", "-o", "/app/time_output.txt", "/bin/sh", "-c", "java Main < /app/input.txt > /app/output.txt"};
                boolean oomKilled = runCode(task.getTimeLimit(), container, cmd);
                // 判断是否超内存
                if (oomKilled) {
                    throw new MemoryLimitExceededException();
                }
                // 从time_output.txt解析运行情况
                String timeOutput = null;
                try {
                    timeOutput = FileUtils.readFileToString(new File(workingDirectoryAbsolutePath + "/time_output.txt"), "UTF-8");
                } catch (IOException e) {
                    throw new SystemException("读取时间输出文件失败", e);
                }
                ExecutionStats executionStats = parseTimeOutput(timeOutput);

                // 更新评测结果 (时间和内存的最大值)
                if (executionStats.getExecutionTime() > result.getTimeUsed()) {
                    result.setTimeUsed(executionStats.getExecutionTime());
                }
                if (executionStats.getMemoryUsage() > result.getMemoryUsed()) {
                    result.setMemoryUsed(executionStats.getMemoryUsage());
                }

                // 判断输出是否正确(output.txt 和 expected_output.txt)
                File output = new File(workingDirectoryAbsolutePath + "/output.txt");
                String outputContent = FileUtils.readFileToString(output, "UTF-8");
                String expectedOutputContent = FileUtils.readFileToString(expectedOutput, "UTF-8");
                if (!outputContent.trim().equals(expectedOutputContent.trim())) {
                    log.info("样例失败{} -- but expected: {}", outputContent, expectedOutputContent);
                    throw new WrongAnswerException("Wrong Answer on test case: "); //TODO 第几个测试用例
                }
            }
            result.setResult("AC"); // TODO 抽取常量
        } catch (SystemException | IOException e) {
            result.setResult("SE");
            result.setErrorMessage(e.getMessage());
        } catch (RuntimeErrorException e) {
            result.setResult("RE");
            result.setErrorMessage(e.getDetails());
        } catch (CompilationException e) {
            result.setResult("CE");
            result.setErrorMessage(e.getMessage());
        } catch (TimeLimitExceededException e) {
            result.setResult("TLE");
            result.setErrorMessage(e.getDetails());
        } catch (MemoryLimitExceededException e) {
            result.setResult("MLE");
            result.setErrorMessage(e.getDetails());
        } catch (WrongAnswerException e) {
            result.setResult("WA");
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            result.setResult("SE");
            result.setErrorMessage("未知错误");
        } finally {
            result.setStatus("Completed");
            messageQueueUtil.sendJudgeResult(result); // 更新状态
            log.info("评测结果: {}", result);

            // 释放容器
            if(container != null) {
                dockerContainerPool.releaseContainer(container);
            }
        }
    }

    /**
     * 编译代码
     * @param dockerContainer 容器
     * @param cmd 编译命令
     */
    public void compileCode(DockerContainer dockerContainer, String... cmd) {
        // 捕获标准输出和错误输出
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        try {
            // 创建命令
            ExecCreateCmdResponse createCmdResponse = dockerContainer.createCmd(cmd);
            // 执行命令
            dockerContainer.executeCmd(createCmdResponse, stdout, stderr);
            // 获取输出结果
            // String stdoutResult = stdout.toString(StandardCharsets.UTF_8).trim();
            String stderrResult = stderr.toString(StandardCharsets.UTF_8).trim();
            // 处理编译结果
            if (!stderrResult.isEmpty()) {
                // 编译错误
                log.error("Compilation errors: {}", stderrResult);
                throw new CompilationException("编译失败:" + stderrResult);
            }
        } catch (InterruptedException | DockerException e) { // 这里算 Judge 系统错误
            throw new SystemException("Compilation process failed", e);
        }
    }

    /**
     * 在容器中运行代码
     *
     * @param timeLimit 时间限制
     * @param dockerContainer 容器
     * @param cmd 命令
     * @return
     */
    public boolean runCode(int timeLimit, DockerContainer dockerContainer, String... cmd) {
        // 获取容器信息
        InspectContainerResponse inspectContainerResponse = dockerContainer.inspectContainer();
        // 创建命令
        ExecCreateCmdResponse createCmdResponse = dockerContainer.createCmd(cmd);
        // 捕获输出
        ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        ByteArrayOutputStream stdErr = new ByteArrayOutputStream();
        // 执行命令
        dockerContainer.execCmdWithTimeout(createCmdResponse, timeLimit, stdOut, stdErr);

        // 输出结果 解析时间和内存 使用 /usr/bin/time -v 命令(需要安装time, 信息默认输出到stderr, 也可以重定向到文件)
        String executionOutput = stdOut.toString(StandardCharsets.UTF_8);
        String errorOutput = stdErr.toString(StandardCharsets.UTF_8);

        log.info("输出：{}", executionOutput);
        log.error("错误：{}", errorOutput);

        if (!errorOutput.isEmpty()) {
            log.error("运行时错误: {}", errorOutput);
            throw new RuntimeErrorException(errorOutput);
        }
        // 是否超内存
        return Boolean.TRUE.equals(inspectContainerResponse.getState().getOOMKilled());
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

}

