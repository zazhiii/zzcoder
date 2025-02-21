package com.zazhi.judger.service;

import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.zazhi.judger.common.exception.*;
import com.zazhi.judger.common.pojo.ExecutionStats;
import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.common.pojo.TestCase;
import com.zazhi.judger.common.utils.DockerUtil;
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

/**
 * @author zazhi
 * @date 2024/12/4
 * @description: Java沙箱
 */
@Service
public class JavaSandBox {

    private static final Logger log = LoggerFactory.getLogger(JavaSandBox.class);
    @Autowired
    private DockerUtil dockerUtil;

    /**
     * ========================================================
     * ================== 以下为沙箱的核心方法 ===================
     * ========================================================
     *
     * @param task   任务
     * @param result 结果
     * @return
     */
    public JudgeResult processTask(JudgeTask task, JudgeResult result) {
        String containerId = null;
        result.setTimeUsed(0);
        result.setMemoryUsed(0);
        try {
            // 创建临时目录用于存放用户提交的代码和文件
            String tempDir = "/tmp/judgeTask_" + task.getTaskId();
            File tempDirectory = new File(tempDir);
            tempDirectory.mkdir();
            // 将用户的代码保存到文件中
            File codeFile = new File(tempDir, "Main.java"); // TODO 抽取常量
            FileUtils.writeStringToFile(codeFile, task.getCode(), "UTF-8");
            // 宿主机的临时目录绝对路径
            String tmpDirAbsPath = tempDirectory.getAbsolutePath();
            // 创建 Docker 容器, 并挂载目录, 设置内存限制
            containerId = dockerUtil.createContainer(tmpDirAbsPath, task.getMemoryLimit(), "java_judger", "java_judger_" + task.getTaskId());// TODO 抽取常量
            // 启动容器
            dockerUtil.startContainer(containerId);
            // 编译用户的代码
            compileCode(containerId, "/bin/sh", "-c", "javac Main.java");
            // 获取测试用例
            List<TestCase> testCases = task.getTestCases();
            for (TestCase testCase : testCases) {
                log.info("测试用例: {}", testCase);
                // 将测试用例保存到文件中
                File inputFile = new File(tempDir, "input.txt");
                File expectedOutput = new File(tempDir, "expected_output.txt");
                FileUtils.writeStringToFile(inputFile, testCase.getInput(), "UTF-8");
                FileUtils.writeStringToFile(expectedOutput, testCase.getExpectedOutput(), "UTF-8");
                // 根据编程语言和任务配置创建 Docker 容器并运行代码
                String[] cmd = {"/usr/bin/time", "-v", "-o", "/app/time_output.txt", "/bin/sh", "-c", "java Main < /app/input.txt > /app/output.txt"};
                boolean oomKilled = runCode(task.getTimeLimit(), containerId, cmd);
                // 判断是否超内存
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
                if (executionStats.getExecutionTime() > result.getTimeUsed()) {
                    result.setTimeUsed(executionStats.getExecutionTime());
                }
                if (executionStats.getMemoryUsage() > result.getMemoryUsed()) {
                    result.setMemoryUsed(executionStats.getMemoryUsage());
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
            result.setResult("AC");
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
            dockerUtil.stopContainer(containerId);
            dockerUtil.removeContainer(containerId);
            FileUtils.deleteQuietly(new File("/tmp/judgeTask_" + task.getTaskId()));
            result.setStatus("finished");
        }
        return result;
    }

    public void compileCode(String containerId, String... cmd) {
        // 捕获标准输出和错误输出
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        try {
            // 创建命令
            ExecCreateCmdResponse execResponse = dockerUtil.createCmd(containerId, cmd);
            // 执行命令
            dockerUtil.executeCmd(execResponse, stdout, stderr);
            // 获取输出结果
            // String stdoutResult = stdout.toString(StandardCharsets.UTF_8).trim();
            String stderrResult = stderr.toString(StandardCharsets.UTF_8).trim();
            // 处理编译结果
            if (!stderrResult.isEmpty()) {
                // 编译错误
                log.error("Compilation errors: {}", stderrResult);
                throw new CompilationException("编译失败:" + stderrResult);
            }
        } catch (InterruptedException | DockerException e) { // 这里算Judge系统错误
            throw new SystemException("Compilation process failed", e);
        }
    }

    /**
     * 在指定容器中运行代码
     */
    public boolean runCode(int timeLimit, String containerId, String... cmd) {
        // 获取容器信息
        InspectContainerResponse inspectContainerResponse = dockerUtil.inspectContainer(containerId);
        // 创建命令
        ExecCreateCmdResponse createCmdResponse = dockerUtil.createCmd(containerId, cmd);
        // 捕获输出
        ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        ByteArrayOutputStream stdErr = new ByteArrayOutputStream();
        // 执行命令
        dockerUtil.execCmdWithTimeout(createCmdResponse, timeLimit, stdOut, stdErr);

        // 输出结果 解析时间和内存 使用 /usr/bin/time -v 命令(需要安装time, 信息默认输出到stderr)
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

