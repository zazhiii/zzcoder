package com.zazhi.judger.service.impl;

import com.zazhi.common.enums.JudgeStatus;
import com.zazhi.common.pojo.entity.JudgeResult;
import com.zazhi.common.pojo.entity.TestCaseResult;
import com.zazhi.judger.common.enums.LanguageType;
import com.zazhi.judger.common.exception.*;
import com.zazhi.judger.common.pojo.*;
import com.zazhi.judger.common.utils.MessageQueueUtil;
import com.zazhi.judger.dockerpool.ContainerPoolExecutor;
import com.zazhi.judger.dockerpool.containers.CodeExecContainer;
import com.zazhi.judger.sandbox.SandBox;
import com.zazhi.judger.sandbox.SandBoxFactory;
import com.zazhi.judger.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lixinhuan
 * @date 2025/7/15
 * @description: JudgeServiceImpl 类实现了 JudgeService 接口，提供判题服务的具体实现
 */
@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {
    private final ContainerPoolExecutor<CodeExecContainer> pool;

    private final MessageQueueUtil messageQueueUtil;

    /**
     * 执行判题任务的方法
     *
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
            for (TestCase testCase : testCases) {
                TestCaseResult caseResult = new TestCaseResult();

                CodeRunResult codeRunResult = new CodeRunResult();
                try {
                    // 超时等待时间比实际时间限制稍微长一点 (1s)
                    Integer timeoutSeconds = (task.getTimeLimit() + 999) / 1000 + 1;
                    String in = testCase.getInput().trim() + "\n";
                    codeRunResult = sandBox.execute(container, in, timeoutSeconds);
                } catch (TimeLimitExceededException e) {
                    caseResult.setStatus(JudgeStatus.TLE);
                } catch (MemoryLimitExceededException e) {
                    caseResult.setStatus(JudgeStatus.MLE);
                } catch (RuntimeErrorException e) {
                    caseResult.setStatus(JudgeStatus.RE);
                    caseResult.setErrorMessage(e.getDetails());
                }

                if (codeRunResult.getTimeUsed() > task.getTimeLimit()) {
                    caseResult.setStatus(JudgeStatus.TLE);
                }
                if (codeRunResult.getMemoryUsed() > task.getMemoryLimit() * 1024L) {
                    caseResult.setStatus(JudgeStatus.MLE);
                }

                // 没有TLE也没有MLE，则比较输出。
                if (caseResult.getStatus() == null) {
                    String expectedOutput = testCase.getOutput();
                    String actualOutput = codeRunResult.getStdout();

                    expectedOutput = expectedOutput.trim();
                    actualOutput = actualOutput.trim();

                    caseResult.setStatus(expectedOutput.equals(actualOutput) ?
                            JudgeStatus.AC : JudgeStatus.WA);

                    maxTimeUsed = Math.max(maxTimeUsed, codeRunResult.getTimeUsed());
                    maxMemoryUsed = Math.max(maxMemoryUsed, codeRunResult.getMemoryUsed());

                    // 单个用例设置耗时和内存使用
                    caseResult.setTimeUsed(codeRunResult.getTimeUsed());
                    caseResult.setMemoryUsed(codeRunResult.getMemoryUsed());
                }

                //  若单个用例没有AC，则设置整个结果的状态
                if (!caseResult.getStatus().equals(JudgeStatus.AC)) {
                    result.setStatus(caseResult.getStatus());
                    result.setErrorMessage(caseResult.getErrorMessage());
                }

                // 非全评测模式下，如果测试点不通过，则不需要继续执行后续测试点
                if (!task.getFullJudge() && !caseResult.getStatus().equals(JudgeStatus.AC)) {
                    return JudgeResult.builder()
                            .taskId(task.getTaskId())
                            .status(caseResult.getStatus())
                            .timeUsed(maxTimeUsed)
                            .memoryUsed(maxMemoryUsed)
                            .fullJudge(false)
                            .build();
                }

                caseResult.setTestCaseId(testCase.getId());
                caseResult.setSubmissionId(task.getTaskId());

                // 发送单个测试点结果到消息队列
                if (task.getFullJudge()) {
                    messageQueueUtil.sendTestCaseResult(caseResult);
                }

                result.getTestCaseResults().add(caseResult);
            }

            // 全部测试点测评完成
            if (result.getStatus() == null) {
                result.setStatus(JudgeStatus.AC);
            }
            result.setTimeUsed(maxTimeUsed);
            result.setMemoryUsed(maxMemoryUsed);
        } catch (SystemException e) {
            result.setStatus(JudgeStatus.SE);
            result.setErrorMessage(e.getMessage());
        } catch (CompilationException e) {
            result.setStatus(JudgeStatus.CE);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            result.setStatus(JudgeStatus.SE);
            result.setErrorMessage("unknown error: " + e.getMessage());
        } finally {
            pool.releaseContainer(container);
        }
        return result;
    }
}
