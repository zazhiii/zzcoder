package com.zazhi.judger.sandbox;

import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.zazhi.judger.common.pojo.CodeRunResult;
import com.zazhi.judger.docker.containers.CodeExecContainer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author lixinhuan
 * @date 2025/7/14
 * @description: JavaSandBox 类实现了 SandBox 接口，用于处理 Java 代码的判题任务
 */
public class JavaSandBox extends SandBox{

    public JavaSandBox(CodeExecContainer codeExecContainer) {
        super(codeExecContainer);
    }

    @Override
    String[] buildCompileCommand(String workDir) {
        return new String[]{"javac", buildCodeFilePath(workDir)};
    }

    @Override
    String[] buildRunCommand(String workDir) {
        return new String[]{"java", workDir, "Main"};
    }

    @Override
    String buildCodeFilePath(String workPath) {
        return workPath + "/" + buildCodeFileName();
    }

    @Override
    String buildCodeFileName() {
        return "Main.java";
    }

}
