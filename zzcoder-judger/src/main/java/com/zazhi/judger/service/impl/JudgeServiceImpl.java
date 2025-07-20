package com.zazhi.judger.service.impl;

import com.zazhi.judger.common.enums.JudgeStatus;
import com.zazhi.judger.common.enums.LanguageType;
import com.zazhi.judger.common.exception.*;
import com.zazhi.judger.common.pojo.*;
import com.zazhi.judger.docker.ContainerPoolExecutor;
import com.zazhi.judger.docker.containers.CodeExecContainer;
import com.zazhi.judger.sandbox.JavaSandBox;
import com.zazhi.judger.sandbox.SandBox;
import com.zazhi.judger.sandbox.SandBoxFactory;
import com.zazhi.judger.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletionException;
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
     * @param task
     * @return
     */
    @Override
    public JudgeResult judge(JudgeTask task) {

        JudgeResult result = new JudgeResult();
        result.setTaskId(task.getTaskId());
        result.setFullJudge(task.getFullJudge());

        CodeExecContainer container = null;
        try {
            container = pool.acquireContainer();
            // 按照不同的语言类型创建对应的沙箱实例
            SandBoxFactory sandBoxFactory = new SandBoxFactory(LanguageType.fromValue(task.getLanguage()));
            SandBox sandBox = sandBoxFactory.createSandBox(container);

            sandBox.saveCode(task.getCode());

            sandBox.compile(container);

            List<TestCase> testCases = task.getTestCases();
            long maxTimeUsed = 0L;
            long maxMemoryUsed = 0L;
            for(TestCase testCase : testCases) {
                TestCaseResult caseResult = new TestCaseResult();
                caseResult.setId(testCase.getId());


                CodeRunResult codeRunResult = new CodeRunResult();
                try {
                    codeRunResult = sandBox.execute(container, testCase.getInput());
                } catch (TimeLimitExceededException e) {
                    caseResult.setStatus(JudgeStatus.TIME_LIMIT_EXCEEDED); // TLE
                } catch (MemoryLimitExceededException e){
                    caseResult.setStatus(JudgeStatus.MEMORY_LIMIT_EXCEEDED); // MLE
                } catch (RuntimeErrorException e){
                    caseResult.setStatus(JudgeStatus.RUNTIME_ERROR); // RE
                    caseResult.setErrorMessage(e.getMessage());
                }

                if(codeRunResult.getTimeUsed() > task.getTimeLimit()){
                    caseResult.setStatus(JudgeStatus.TIME_LIMIT_EXCEEDED);
                }
                if(codeRunResult.getMemoryUsed() > task.getMemoryLimit()) {
                    caseResult.setStatus(JudgeStatus.MEMORY_LIMIT_EXCEEDED);
                }

                // 没有TLE也没有MLE，则比较输出。
                if(caseResult.getStatus() == null){
                    String expectedOutput = testCase.getOutput();
                    String actualOutput = codeRunResult.getStdout();

                    expectedOutput = expectedOutput.trim();
                    actualOutput = actualOutput.trim();

                    caseResult.setStatus(expectedOutput.equals(actualOutput) ?
                            JudgeStatus.ACCEPTED : JudgeStatus.WRONG_ANSWER);

                    maxTimeUsed = Math.max(maxTimeUsed, codeRunResult.getTimeUsed());
                    maxMemoryUsed = Math.max(maxMemoryUsed, codeRunResult.getMemoryUsed());

                    // 单个用例设置耗时和内存使用
                    caseResult.setTimeUsed(codeRunResult.getTimeUsed());
                    caseResult.setMemoryUsed(codeRunResult.getMemoryUsed());
                }

                //  若单个用例没有AC，则设置整个结果的状态
                if(!caseResult.getStatus().equals(JudgeStatus.ACCEPTED)) {
                    result.setStatus(caseResult.getStatus());
                }

                // 非全评测模式下，如果测试点不通过，则不需要继续执行后续测试点
                if(!task.getFullJudge() && !caseResult.getStatus().equals(JudgeStatus.ACCEPTED)){
                    return JudgeResult.builder()
                            .taskId(task.getTaskId())
                            .status(caseResult.getStatus())
                            .timeUsed(maxTimeUsed)
                            .memoryUsed(maxMemoryUsed)
                            .fullJudge(false)
                            .build();
                }
                result.getTestCaseResults().add(caseResult);
            }

            // 全部测试点测评完成
            if(result.getStatus() == null) {
                result.setStatus(JudgeStatus.ACCEPTED);
            }
            result.setTimeUsed(maxTimeUsed);
            result.setMemoryUsed(maxMemoryUsed);
        } catch (SystemException e) {
            result.setStatus(JudgeStatus.SYSTEM_ERROR);
            result.setErrorMessage(e.getMessage());
        } catch (CompilationException e){
            result.setStatus(JudgeStatus.COMPILE_ERROR);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e){
            result.setStatus(JudgeStatus.SYSTEM_ERROR);
            result.setErrorMessage("unknown error: " + e.getMessage());
        }finally {
            pool.releaseContainer(container);
        }
        return result;
    }
}
