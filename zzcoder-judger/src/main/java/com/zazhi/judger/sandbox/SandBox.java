package com.zazhi.judger.sandbox;

import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.zazhi.judger.common.exception.*;
import com.zazhi.judger.common.pojo.CodeRunResult;
import com.zazhi.judger.docker.containers.CodeExecContainer;
import com.zazhi.judger.docker.pojo.CmdExecResult;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public abstract class SandBox {

    protected CodeExecContainer container;

    public SandBox(CodeExecContainer codeExecContainer) {
        this.container = codeExecContainer;
    }

    /**
     * 编译代码（如果是编译型语言）
     * @param container 当前容器对象
     */
    public void compile(CodeExecContainer container){
        String[] compileCommand = buildCompileCommand();
        CmdExecResult res = null;
        try {
            res = container.execCmd(compileCommand, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new SystemException("编译代码时被中断", e);
        }
        if (!res.getStderr().isEmpty()) {
            throw new CompilationException("编译错误: " + res.getStderr());
        }
    }

    /**
     * 构建编译命令
     * @return 编译命令数组
     */
    abstract String[] buildCompileCommand();

    private String[] buildTimeCommand() {
        return new String[]{"time", "-f", "__TIME__:%U %S %E %M"};
    }

    private String[] combineCommands(String[] timeCommand, String[] runCommand) {
        String[] combined = new String[timeCommand.length + runCommand.length];
        System.arraycopy(timeCommand, 0, combined, 0, timeCommand.length);
        System.arraycopy(runCommand, 0, combined, timeCommand.length, runCommand.length);
        return combined;
    }

    /**
     * 执行代码
     * @param container 当前容器对象
     * @param stdin 标准输入流
     * @return 代码运行结果
     */
    public CodeRunResult execute(CodeExecContainer container, String stdin) {
        String[] cmd = combineCommands(buildTimeCommand(), buildRunCommand());
        CmdExecResult res = null;
        try { // TODO: 超时等待时间抽取为配置
            res = container.execCmd(cmd, stdin, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new SystemException("运行代码时出错", e);
        }

        if(res.isTimeout()) throw new TimeLimitExceededException();

        // 检查容器是否oom
        if (Boolean.TRUE.equals(container.inspectContainer().getState().getOOMKilled())) {
            throw new MemoryLimitExceededException();
        }

        if (res.getStderr().startsWith("__TIME__:")) {
            String[] parts = res.getStderr().substring(9).trim().split(" ");
            long timeUsed = (long)(Double.parseDouble(parts[2].split(":")[1]) * 1000);
            long memoryUsed = Long.parseLong(parts[3]);
            return new CodeRunResult(res.getStdout(), timeUsed, memoryUsed);
        }else{
            throw new RuntimeErrorException(res.getStderr().split("__TIME__:")[0].trim());
        }
    }

    /**
     * 构建运行命令
     * @return 运行命令数组
     */
    abstract String[] buildRunCommand();

    /**
     * 构建代码文件名
     * @return 代码文件名
     */
    abstract String buildCodeFileName();

    /**
     * 保存代码到指定工作目录
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
