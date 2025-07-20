package com.zazhi.judger.sandbox;

import com.zazhi.judger.common.enums.LanguageType;
import com.zazhi.judger.docker.containers.CodeExecContainer;

/**
 * @author lixh
 * @since 2025/7/18 23:18
 */
public class SandBoxFactory {

    private final LanguageType language;

    public SandBoxFactory(LanguageType language) {
        this.language = language;
    }

    public SandBox createSandBox(CodeExecContainer container) {
        switch (language) {
            case JAVA:
                return new JavaSandBox(container);
//            case PYTHON:
//                return new PythonSandBox();
//            case C:
//                return new CSandBox();
//            case CPP:
//                return new CppSandBox();
//            case GO:
//                return new GoSandBox();
            default:
                throw new IllegalArgumentException("不支持的语言类型: " + language);
        }
    }
}
