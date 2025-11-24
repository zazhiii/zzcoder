package com.zazhi.zzcoder.listener;

import com.zazhi.zzcoder.common.pojo.entity.JudgeResult;
import com.zazhi.zzcoder.common.pojo.entity.TestCaseResult;
import com.zazhi.zzcoder.controller.oj.JudgeSseController;
import com.zazhi.zzcoder.service.JudgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class JudgeResultListener {
    private final JudgeService judgeService;

    private final JudgeSseController judgeSseController;

    @RabbitListener(queues = "judge_result_queue")
    public void receiveJudgeResult(JudgeResult judgeResult) {
        log.info("JUDGE RESULT: {}", judgeResult);
        // 通过SSE向前端发送判题结果
        judgeSseController.sendStatus(judgeResult.getTaskId(), judgeResult.getStatus().getName());
        // 更新数据库
        judgeService.updateSubmission(judgeResult);
    }

    @RabbitListener(queues = "test_case_result_queue")
    public void receiveTestCaseResult(TestCaseResult testCaseResult) {
        log.info("TEST CASE RESULT: {}", testCaseResult);
        judgeSseController.sendTestCaseStatus(testCaseResult.getSubmissionId(), testCaseResult);
        judgeService.addTestCaseResult(testCaseResult);
    }
}
