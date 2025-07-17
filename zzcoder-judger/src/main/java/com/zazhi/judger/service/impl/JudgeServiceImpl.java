package com.zazhi.judger.service.impl;

import com.zazhi.judger.common.enums.LanguageType;
import com.zazhi.judger.common.pojo.CodeRunResult;
import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.docker.ContainerPoolExecutor;
import com.zazhi.judger.docker.containers.CodeExecContainer;
import com.zazhi.judger.sandbox.JavaSandBox;
import com.zazhi.judger.sandbox.SandBox;
import com.zazhi.judger.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author lixinhuan
 * @date 2025/7/15
 * @description: JudgeServiceImpl 类实现了 JudgeService 接口，提供判题服务的具体实现
 */
@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {

    private final ContainerPoolExecutor<CodeExecContainer> pool;

    /**
     * 执行判题任务的方法
     * @param judgeTask
     * @return
     */
    @Override
    public JudgeResult judge(JudgeTask judgeTask) {
        CodeExecContainer container = null;
        try {
            container = pool.acquireContainer();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        JudgeResult judgeResult = new JudgeResult();

        try {
            SandBox sandBox = switch (LanguageType.fromValue(judgeTask.getLanguage())){
                case JAVA -> new JavaSandBox(container);
                // 可以添加其他语言的处理逻辑
                default -> throw new IllegalArgumentException("Unsupported language: " + judgeTask.getLanguage());
            };

            sandBox.saveCode(judgeTask.getCode());

            String err = sandBox.compile(container);

            if(err != null && !err.isEmpty()){
                return JudgeResult.compileError(err);
            }

            // TODO: 运行代码
//            ByteArrayInputStream in = new ByteArrayInputStream(judgeTask.getTestCases().get(0).getInput().getBytes());
//            CodeRunResult runRes = sandBox.execute(container, in, judgeTask.getTimeLimit(), TimeUnit.MILLISECONDS);

//            return JudgeResult.builder()
//                    .memoryUsed(runRes.getMemoryUsed())
//                    .timeUsed(runRes.getTimeUsed())
//                    .build();

            return null;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseContainer(container);
        }
    }
}
