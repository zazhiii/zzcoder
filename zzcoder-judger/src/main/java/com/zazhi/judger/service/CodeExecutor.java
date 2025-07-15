package com.zazhi.judger.service;

import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.zazhi.judger.common.pojo.CodeRunResult;
import com.zazhi.judger.docker.containers.CodeExecContainer;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public abstract class CodeExecutor {
    /**
     * 编译代码（如果是编译型语言）
     * @param container 当前容器对象
     * @return 错误信息，成功返回 ""
     */
    public String compile(CodeExecContainer container){
        String[] compileCommand = buildCompileCommand(container.getContainerWorkingDir());
        ExecCreateCmdResponse resp = container.createCmd(compileCommand);
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        try {
            container.execCmdAsync(resp, new ByteArrayOutputStream(), stderr, null)
                    .awaitCompletion(10, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("编译代码时被中断", e);
        }
        return stderr.toString(StandardCharsets.UTF_8);
    }

    /**
     * 构建编译命令
     * @param workDir 容器工作目录
     * @return 编译命令数组
     */
    abstract String[] buildCompileCommand(String workDir);

    /**
     * 执行代码
     * @param container 当前容器对象
     * @param stdin 标准输入流
     * @param timeout 执行超时时间
     * @param unit 超时时间单位
     * @return 代码运行结果
     */
    public CodeRunResult execute(CodeExecContainer container, InputStream stdin, long timeout, TimeUnit unit) {
        String[] runCommand = buildRunCommand(buildCodeFilePath(container.getContainerWorkingDir()));
        ExecCreateCmdResponse resp = container.createCmd(runCommand);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        boolean ok = false;

        try {
            ok = container.execCmdAsync(resp, out, err, stdin).awaitCompletion(timeout, unit);
        } catch (InterruptedException e) {
            return CodeRunResult.error("执行中断");
        }

        if (!ok) return CodeRunResult.timeout();

        String errStr = err.toString(StandardCharsets.UTF_8);
        String outStr = out.toString(StandardCharsets.UTF_8);
        if (errStr.startsWith("__TIME__:")) {
            String[] parts = errStr.substring(9).trim().split(" ");
            long timeUsed = (long)(Double.parseDouble(parts[2].split(":")[1]) * 1000);
            long memoryUsed = Long.parseLong(parts[3]) / 1024;
            return CodeRunResult.success(outStr, timeUsed, memoryUsed);
        }

        return CodeRunResult.error(errStr.split("__TIME__:")[0].trim());
    }

    /**
     * 构建运行命令
     * @param codePath 容器内代码路径
     * @return 运行命令数组
     */
    abstract String[] buildRunCommand(String codePath);

    /**
     * 构建代码文件路径
     * @param workPath
     * @return
     */
    abstract String buildCodeFilePath(String workPath);

    /**
     * 构建代码文件名
     * @return 代码文件名
     */
    abstract String buildCodeFileName();

    /**
     * 保存代码到指定路径
     * @param workPath
     * @param code
     * @throws Exception
     */
    public void save (String workPath, String code) throws Exception {
        File codeFile = new File(buildCodeFilePath(workPath));
        FileUtils.writeStringToFile(codeFile, code, StandardCharsets.UTF_8);
    }
}
