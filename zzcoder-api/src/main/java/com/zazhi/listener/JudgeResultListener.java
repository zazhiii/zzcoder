package com.zazhi.listener;

import com.zazhi.common.pojo.entity.JudgeResult;
import com.zazhi.service.JudgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class JudgeResultListener {

    private final JudgeService judgeService;

    @RabbitListener(queues = "judge_result_queue")
    public void receiveJudgeResult(JudgeResult judgeResult) {
        log.info("JUDGE RESULT: {}", judgeResult);
        judgeService.updateSubmission(judgeResult);
    }
}
