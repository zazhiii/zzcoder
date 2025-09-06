package com.zazhi.judger.sandbox;

import com.zazhi.judger.common.exception.*;
import com.zazhi.judger.common.pojo.CodeRunResult;
import com.zazhi.judger.dockerpool.containers.CodeExecContainer;
import com.zazhi.judger.dockerpool.pojo.CmdExecResult;

import java.io.*;
import java.util.concurrent.TimeUnit;

public abstract class SandBox {
    protected CodeExecContainer container;

    public SandBox(CodeExecContainer codeExecContainer) {
        this.container = codeExecContainer;
    }

    /**
     * 编译代码（如果是编译型语言）
     *
     * @param container 当前容器对象
     */
    public void compile(CodeExecContainer container) {
        String[] compileCommand = buildCompileCommand();
        CmdExecResult res;
        try {
            res = container.execCmd(compileCommand);
        } catch (InterruptedException e) {
            throw new SystemException("编译代码时被中断", e);
        }
        if (!res.getStderr().isEmpty()) {
            throw new CompilationException("编译错误: " + res.getStderr());
        }
    }

    /**
     * 构建编译命令
     *
     * @return 编译命令数组
     */
    abstract String[] buildCompileCommand();

    private String[] buildTimeCommand(Integer seconds) {
        return new String[]{"time", "-f", "__TIME__:%E %M", "timeout", seconds + "s"};
    }

    private String[] combineCommands(String[] timeCommand, String[] runCommand) {
        String[] combined = new String[timeCommand.length + runCommand.length];
        System.arraycopy(timeCommand, 0, combined, 0, timeCommand.length);
        System.arraycopy(runCommand, 0, combined, timeCommand.length, runCommand.length);
        return combined;
    }

    /**
     * 执行代码
     *
     * @param container      当前容器对象
     * @param stdin          标准输入流
     * @param timeoutSeconds 时间限制（秒）
     * @return 代码运行结果
     */
    public CodeRunResult execute(CodeExecContainer container, String stdin, Integer timeoutSeconds) {
        // 让linux命令的超时时间再多1秒，防止在Java层面的超时等待未完成时返回错误信息，导致判断为运行时错误
        String[] cmd = combineCommands(buildTimeCommand(timeoutSeconds + 1), buildRunCommand());
        CmdExecResult res;
        try {
            res = container.execCmd(cmd, stdin, timeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new SystemException("运行代码时出错", e);
        }

        if(res.getTimeout()) {
            throw new TimeLimitExceededException();
        }

        // 检查容器是否oom
        if (Boolean.TRUE.equals(container.inspectContainer().getState().getOOMKilled())) {
            throw new MemoryLimitExceededException();
        }

        if (res.getStderr().startsWith("__TIME__:")) {
            String[] parts = res.getStderr().substring("__TIME__".length() + 1).trim().split(" ");
            long timeUsed = (long) (Double.parseDouble(parts[0].split(":")[1]) * 1000);
            long memoryUsed = Long.parseLong(parts[1]);
            return new CodeRunResult(res.getStdout(), null, timeUsed, memoryUsed);
        } else {
            throw new RuntimeErrorException(res.getStderr().split("__TIME__:")[0].trim());
        }
    }

    /**
     * 构建运行命令
     *
     * @return 运行命令数组
     */
    abstract String[] buildRunCommand();

    /**
     * 构建代码文件名
     *
     * @return 代码文件名
     */
    abstract String buildCodeFileName();

    /**
     * 保存代码到指定工作目录
     *
     * @param code 代码内容
     */
    public void saveCode(String code) {
        File file = new File(container.getHostWorkingDir() + "/" + buildCodeFileName());
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(code);
        } catch (IOException e) {
            throw new SystemException("保存代码文件失败", e);
        }
    }
}
