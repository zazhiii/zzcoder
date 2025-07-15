package com.zazhi.judger.service;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author lixinhuan
 * @date 2025/7/10
 * @description: JavaExecutor 类用于执行 Java 代码的编译和运行
 */
@Component
public class JavaExecutor extends CodeExecutor {

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
        return workPath + File.separator + buildCodeFileName();
    }

    @Override
    String buildCodeFileName() {
        return "Main.java";
    }


}

